package com.example.TaskFlow.Message.Producer;

import com.example.TaskFlow.DTO.Event.DlqEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DlqEventProducer {

    private final KafkaTemplate<String, DlqEvent> dlqKafkaTemplate;

    public void sendDlqEvent(String topic, DlqEvent dlqEvent) {
        dlqKafkaTemplate.send(topic, dlqEvent);
        log.info("DLQ Event was sent to Kafka DLQ topic [{}]: {}",
                topic,
                dlqEvent);
    }

}
