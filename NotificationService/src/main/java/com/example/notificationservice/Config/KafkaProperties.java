package com.example.notificationservice.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.kafka")
public record KafkaProperties(
        String topicLogs,
        String bootstrapServers
) {}
