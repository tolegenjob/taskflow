package com.example.TaskFlow.Message.Consumer;

import com.example.TaskFlow.DTO.Event.LogEntryEvent;
import com.example.TaskFlow.Service.LogEntryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogEntryEventConsumer {

    private final LogEntryService logEntryService;

    @KafkaListener(
            topics = "${spring.kafka.topic.logs}",
            groupId = "${spring.kafka.consumer.logs-group-id}",
            containerFactory = "${spring.kafka.consumer.logs-container-factory}"
    )
    public void receiveLogEntryEvent(LogEntryEvent logEntryEvent) {
        log.info("Received LogEntryEvent: {} {} {}",
                logEntryEvent.level(),
                logEntryEvent.message(),
                logEntryEvent.context());
        logEntryService.createLogEntry(logEntryEvent);
    }

}
