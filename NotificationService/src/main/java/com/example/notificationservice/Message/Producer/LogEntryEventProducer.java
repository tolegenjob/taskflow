package com.example.notificationservice.Message.Producer;

import com.example.notificationservice.DTO.Event.LogEntryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogEntryEventProducer {

    private final KafkaTemplate<String, LogEntryEvent> logsKafkaTemplate;

    public void sendLogEntryEvent(String topic, LogEntryEvent logEntryEvent) {
        logsKafkaTemplate.send(topic, logEntryEvent);
        log.info("LogEntryEvent was sent to Kafka topic [{}]: {} {}",
                topic,
                logEntryEvent.level(),
                logEntryEvent.message());
    }
}
