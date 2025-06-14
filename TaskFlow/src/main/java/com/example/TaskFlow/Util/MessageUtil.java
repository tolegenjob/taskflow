package com.example.TaskFlow.Util;

import com.example.TaskFlow.DTO.Event.DlqEvent;
import com.example.TaskFlow.DTO.Event.Event;
import com.example.TaskFlow.DTO.Event.LogEntryEvent;
import com.example.TaskFlow.DTO.Event.NotificationEvent;
import com.example.TaskFlow.Enum.EntityType;
import com.example.TaskFlow.Enum.EventType;
import com.example.TaskFlow.Enum.LogLevel;
import com.example.TaskFlow.Message.Producer.DlqEventProducer;
import com.example.TaskFlow.Message.Producer.EventLogProducer;
import com.example.TaskFlow.Message.Producer.LogEntryEventProducer;
import com.example.TaskFlow.Message.Producer.RedisMessageProducer;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
public class MessageUtil {

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

    public static void sendEventLogToKafka(EventLogProducer eventLogProducer,
                                           String topic,
                                           EventType eventType,
                                           Long entityId,
                                           EntityType entityType,
                                           Map<String, Object> payload ) {
        Event event = new Event(
                eventType, entityId, entityType, payload
        );
        log.info("Event was created: {}", event);
        eventLogProducer.sendEventLog(topic, event);
    }

    public static void sendDlqToKafka(DlqEventProducer dlqEventProducer,
                                      String topic,
                                      Event event,
                                      String message) {
        DlqEvent dlqEvent = new DlqEvent(
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

    public static void sendNotificationEventToRedis(RedisMessageProducer redisMessageProducer,
                                                    Long entityId,
                                                    String title,
                                                    String status) {
        NotificationEvent event = new NotificationEvent(
                entityId,
                title,
                status
        );
        log.info("Notification event created: {}", event);
        redisMessageProducer.publishUpdate(event);
    }

}
