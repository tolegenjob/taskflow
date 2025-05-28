package com.example.TaskFlow.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.rabbitmq")
public record RabbitMQProperties(
        Retry retry,
        History history,
        Notifications notifications,
        Integer ttl
) {

    public record Retry(
            long backoffPeriod,
            int maxAttempts
    ) {}

    public record History(
            String exchange,
            String queue,
            String routingKey
    ) {}

    public record Notifications(
            Exchange exchange,
            Queue queue,
            RoutingKey routingKey
    ) {

        public record Exchange(
                String direct,
                String fanout,
                String topic,
                String headers,
                String dlx
        ) {}

        public record Queue(
                String general,
                String topic,
                String headers,
                Audit audit,
                Error error,
                String dlx
        ) {
            public record Audit(
                    String fanout
            ) {}
            public record Error(
                    String topic
            ) {}
        }

        public record RoutingKey(
                String create,
                String error,
                String delete
        ) {}
    }
}

