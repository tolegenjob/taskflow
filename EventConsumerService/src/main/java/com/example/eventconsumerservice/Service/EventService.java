package com.example.eventconsumerservice.Service;

import com.example.eventconsumerservice.Config.KafkaProperties;
import com.example.eventconsumerservice.Config.RabbitMQProperties;
import com.example.eventconsumerservice.DTO.Event.CommentNotificationEvent;
import com.example.eventconsumerservice.DTO.Event.IncomeEvent;
import com.example.eventconsumerservice.Enum.CommentAction;
import com.example.eventconsumerservice.Enum.EntityType;
import com.example.eventconsumerservice.Enum.EventType;
import com.example.eventconsumerservice.Exception.BadEventException;
import com.example.eventconsumerservice.Message.Producer.CommentNotificationProducer;
import com.example.eventconsumerservice.Message.Producer.DlqEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import static com.example.eventconsumerservice.Util.MessageUtil.sendCommentNotificationToRabbitMQ;
import static com.example.eventconsumerservice.Util.MessageUtil.sendDlqToKafka;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final ProcessedEventService processedEventService;
    private final ArchivedProjectService archivedProjectService;
    private final KafkaProperties kafkaProperties;
    private final DlqEventProducer dlqEventProducer;
    private final CommentNotificationProducer commentNotificationProducer;
    private final RabbitMQProperties rabbitMQProperties;

    public void handleEvent(IncomeEvent event, Acknowledgment ack) {
        switch (event.entityType()) {
            case EntityType.TASK -> handleTaskEvent(event);
            case EntityType.COMMENT -> handleCommentEvent(event);
            case EntityType.PROJECT -> handleProjectEvent(event);
            default -> {
                sendDlqToKafka(dlqEventProducer,
                        kafkaProperties.topic().dlq(),
                        event,
                        "Invalid EntityType");
                throw new BadEventException("Invalid EntityType");
            }
        }
        processedEventService.createProcessedEvent(event);
        ack.acknowledge();
    }

    private void handleTaskEvent(IncomeEvent event) {
        log.info("Handled task event: {}", event);
    }

    private void handleCommentEvent(IncomeEvent event) {
        CommentAction action;
        switch (event.eventType()) {
            case EventType.COMMENT_CREATED -> action = CommentAction.ADDED;
            case EventType.COMMENT_UPDATED -> action = CommentAction.UPDATED;
            case EventType.COMMENT_DELETED -> action = CommentAction.DELETED;
            default -> {
                sendDlqToKafka(dlqEventProducer,
                        kafkaProperties.topic().dlq(),
                        event,
                        "Invalid CommentAction");
                throw new BadEventException("Invalid CommentAction");
            }
        }
        CommentNotificationEvent commentNotificationEvent = new CommentNotificationEvent(
                (String) event.payload().get("content"),
                action,
                ((Number) event.payload().get("taskId")).longValue(),
                ((Number) event.payload().get("taskAuthorId")).longValue(),
                ((Number) event.payload().get("userId")).longValue()
        );
        sendCommentNotificationToRabbitMQ(
                commentNotificationProducer,
                commentNotificationEvent,
                rabbitMQProperties.exchange().direct(),
                rabbitMQProperties.routingKey().direct());
        log.info("Handled comment event: {}", event);
    }

    private void handleProjectEvent(IncomeEvent event) {
        if(event.eventType().equals(EventType.PROJECT_ARCHIVED)) {
            archivedProjectService.createArchivedProject(event.payload());
        } else {
            log.warn("EventType is not PROJECT_ARCHIVED, but: {}", event.eventType());
        }
        log.info("Handled project event: {}", event);
    }

}
