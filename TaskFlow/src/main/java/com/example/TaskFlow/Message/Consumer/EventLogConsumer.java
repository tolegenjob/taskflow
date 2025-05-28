package com.example.TaskFlow.Message.Consumer;

import com.example.TaskFlow.DTO.Event.Event;
import com.example.TaskFlow.Service.EventLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventLogConsumer {

    private final EventLogService eventLogService;

    @KafkaListener(
            topics = "${spring.kafka.topic.events}",
            groupId = "${spring.kafka.consumer.events-group-id}",
            containerFactory = "${spring.kafka.consumer.events-container-factory}"
    )
    public void receiveEvent(Event event) {
        log.info("Received Event: {} from topic kafka.topic.events",
                event);
        eventLogService.createEventLog(event);
    }

}
