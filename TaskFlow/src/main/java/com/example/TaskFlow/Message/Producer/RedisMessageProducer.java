package com.example.TaskFlow.Message.Producer;

import com.example.TaskFlow.Config.RedisProperties;
import com.example.TaskFlow.DTO.Event.NotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisMessageProducer {

    private final StringRedisTemplate redisTemplate;
    private final RedisProperties redisProperties;
    private final ObjectMapper objectMapper;

    public void publishUpdate(NotificationEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            redisTemplate.convertAndSend(redisProperties.patternTopic(), json);
            log.info("send notification event {} to redis", event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Event serialization error", e);
        }
    }

}
