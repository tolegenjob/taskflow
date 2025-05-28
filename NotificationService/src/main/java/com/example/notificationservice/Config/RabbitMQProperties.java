package com.example.notificationservice.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.rabbitmq")
public record RabbitMQProperties(
        Queue queue,
        Exchange exchange,
        RoutingKey routingKey,
        Retry retry
) {

    public record Queue(
            Notifications notifications,
            Audit audit,
            Error error,
            Dlx dlx,
            Integer ttl
    ) {
        public record Notifications(
                String general,
                String topic,
                String headers,
                String comment
        ) {}

        public record Audit(
                String fanout
        ) {}

        public record Error(
                String topic
        ) {}

        public record Dlx(
                String notifications,
                String comment
        ) {}
    }

    public record Exchange(
            String direct,
            String fanout,
            String topic,
            String headers,
            String dlx
    ) {}

    public record RoutingKey(
            String direct,
            String comment,
            Topic topic
    ) {
        public record Topic(
                String general,
                String error
        ) {}
    }

    public record Retry(
        long backoffPeriod,
        int maxAttempts
    ) {}

}

