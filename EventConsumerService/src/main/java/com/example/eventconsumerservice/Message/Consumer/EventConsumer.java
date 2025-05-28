package com.example.eventconsumerservice.Message.Consumer;

import com.example.eventconsumerservice.DTO.Event.DlqIncomeEvent;
import com.example.eventconsumerservice.DTO.Event.IncomeEvent;
import com.example.eventconsumerservice.Service.DlqEventService;
import com.example.eventconsumerservice.Service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventConsumer {

    private final EventService eventService;
    private final DlqEventService dlqEventService;

    @KafkaListener(topics = "${spring.kafka.topic.events}", groupId = "${spring.kafka.consumer.events-group-id}")
    public void receiveEvent(IncomeEvent event, Acknowledgment ack) {
        log.info("Received Event: {}",
                event);
        eventService.handleEvent(event, ack);
    }

    @KafkaListener(topics = "${spring.kafka.topic.dlq}", groupId = "${spring.kafka.consumer.dlq-group-id}")
    public void receiveEvent(DlqIncomeEvent event, Acknowledgment ack) {
        log.info("Received DlqEvent: {}",
                event);
        dlqEventService.createDlqEvent(event);
        ack.acknowledge();
    }

}
