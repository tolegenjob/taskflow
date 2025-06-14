package com.example.notificationservice.Message.Consumer;

import com.example.notificationservice.DTO.Event.NotificationEvent;
import com.example.notificationservice.Service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisMessageConsumer implements MessageListener {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String json = message.toString();
        try {
            NotificationEvent event = objectMapper.readValue(json, NotificationEvent.class);
            log.info("Received event: {}", event);
            notificationService.createNotification(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Event deserialization error", e);
        }

    }
}
