package com.example.TaskFlow.Message.Producer;

import com.example.TaskFlow.DTO.Event.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventLogProducer {

    private final KafkaTemplate<String, Event> eventKafkaTemplate;

    public void sendEventLog(String topic, Event event) {
        eventKafkaTemplate.send(topic, event);
        log.info("Event was sent to Kafka topic [{}]: {}",
                topic,
                event);
    }

}
