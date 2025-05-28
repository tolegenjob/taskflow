package com.example.TaskFlow.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.kafka")
public record KafkaProperties(
        Topic topic,
        String bootstrapServers,
        Consumer consumer
) {
    public record Topic(
            String logs,
            String events,
            String dlq
    ) {}
    public record Consumer(
            String logsGroupId,
            String eventsGroupId,
            String logsContainerFactory,
            String eventsContainerFactory
    ) {}
}
