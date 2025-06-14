package com.example.eventconsumerservice.Message.Consumer;

import com.example.eventconsumerservice.DTO.Event.DlqIncomeEvent;
import com.example.eventconsumerservice.Service.DlqEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DlqEventConsumer {

    private final DlqEventService dlqEventService;

    @KafkaListener(topics = "${spring.kafka.topic.dlq}", groupId = "${spring.kafka.consumer.dlq-group-id}")
    public void receiveEvent(DlqIncomeEvent event, Acknowledgment ack) {
        log.info("Received DlqEvent: {}",
                event);
        dlqEventService.createDlqEvent(event);
        ack.acknowledge();
    }

}
