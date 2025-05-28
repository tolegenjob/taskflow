package com.example.eventconsumerservice.Message.Producer;

import com.example.eventconsumerservice.DTO.Event.DlqIncomeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DlqEventProducer {

    private final KafkaTemplate<String, DlqIncomeEvent> dlqKafkaTemplate;

    public void sendDlqEvent(String topic, DlqIncomeEvent dlqIncomeEvent) {
        dlqKafkaTemplate.send(topic, dlqIncomeEvent);
        log.info("DLQ Event was sent to Kafka DLQ topic [{}]: {}",
                topic,
                dlqIncomeEvent);
    }

}
