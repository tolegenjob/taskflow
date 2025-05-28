package com.example.notificationservice.Util;

import com.example.notificationservice.DTO.Event.LogEntryEvent;
import com.example.notificationservice.Enum.LogLevel;
import com.example.notificationservice.Message.Producer.LogEntryEventProducer;
import lombok.extern.slf4j.Slf4j;

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
}
