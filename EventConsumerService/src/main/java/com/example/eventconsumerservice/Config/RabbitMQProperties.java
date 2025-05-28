package com.example.eventconsumerservice.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.rabbitmq")
public record RabbitMQProperties(
        Queue queue,
        Exchange exchange,
        RoutingKey routingKey,
        Retry retry
) {

    public record Queue(
            String notifications,
            String dlxNotifications,
            Integer ttl
    ) {}

    public record Exchange(
            String direct,
            String dlx
    ) {}

    public record RoutingKey(
            String direct
    ) {}

    public record Retry(
            long backoffPeriod,
            int maxAttempts
    ) {}

}


