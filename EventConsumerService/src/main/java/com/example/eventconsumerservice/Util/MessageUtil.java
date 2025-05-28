package com.example.eventconsumerservice.Util;

import com.example.eventconsumerservice.DTO.Event.CommentNotificationEvent;
import com.example.eventconsumerservice.DTO.Event.DlqIncomeEvent;
import com.example.eventconsumerservice.DTO.Event.IncomeEvent;
import com.example.eventconsumerservice.DTO.Event.LogEntryEvent;
import com.example.eventconsumerservice.Enum.LogLevel;
import com.example.eventconsumerservice.Message.Producer.CommentNotificationProducer;
import com.example.eventconsumerservice.Message.Producer.DlqEventProducer;
import com.example.eventconsumerservice.Message.Producer.LogEntryEventProducer;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
public class MessageUtil {

    public static void sendCommentNotificationToRabbitMQ(CommentNotificationProducer producer,
                                                         CommentNotificationEvent event,
                                                         String exchange,
                                                         String routingKey) {
        producer.sendCommentNotificationEvent(exchange, routingKey, event);
    }

    public static void sendLogEntryEventToKafka(LogEntryEventProducer logEntryEventProducer,
                                                String topic,
                                                LogLevel level,
                                                String message,
                                                Map<String, Object> context) {

        LogEntryEvent logEntryEvent = new LogEntryEvent(
                level, message, context
        );
        log.info("LogEntry event created: {}", logEntryEvent);
        logEntryEventProducer.sendLogEntryEvent(
                topic,
                logEntryEvent
        );
    }

    public static void sendDlqToKafka(DlqEventProducer dlqEventProducer,
                                      String topic,
                                      IncomeEvent event,
                                      String message) {
        DlqIncomeEvent dlqEvent = new DlqIncomeEvent(
                event.eventType(),
                event.entityId(),
                event.entityType(),
                event.payload(),
                message,
                LocalDateTime.now()
        );
        log.info("Dlq event created: {}", dlqEvent);
        dlqEventProducer.sendDlqEvent(topic, dlqEvent);
    }

}
