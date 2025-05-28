package com.example.TaskFlow.Message.Producer;

import com.example.TaskFlow.DTO.Event.TaskHistoryEvent;
import com.example.TaskFlow.DTO.Event.TaskHistoryNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskHistoryEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendTaskHistoryEvent(String exchange, String routingKey, TaskHistoryEvent taskHistoryEvent) {
        rabbitTemplate.convertAndSend(exchange, routingKey, taskHistoryEvent);
        log.info("Sending task history event: {} {} with exchange: {}, routing-key: {}",
                taskHistoryEvent.taskId(),
                taskHistoryEvent.action(),
                exchange,
                routingKey);
    }

    public void sendTaskHistoryNotificationEvent(String exchange, String routingKey, TaskHistoryNotificationEvent event) {
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
        log.info("Sending task history notification event: {} {} {} with exchange: {}, routing-key: {}",
                event.taskId(),
                event.title(),
                event.status(),
                exchange,
                routingKey);
    }

}
