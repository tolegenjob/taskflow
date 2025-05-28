package com.example.TaskFlow.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.Map;


@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    private final RabbitMQProperties rabbitMQProperties;

    @Bean
    public Queue historyQueue() {
        return new Queue(rabbitMQProperties.history().queue(), true);
    }

    @Bean
    public DirectExchange historyExchange() {
        return new DirectExchange(rabbitMQProperties.history().exchange());
    }

    @Bean
    public Binding historyBinding(Queue historyQueue, DirectExchange historyExchange) {
        return BindingBuilder
                .bind(historyQueue)
                .to(historyExchange)
                .with(rabbitMQProperties.history().routingKey());
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RetryTemplate retryTemplate() {
        FixedBackOffPolicy backOff = new FixedBackOffPolicy();
        backOff.setBackOffPeriod(rabbitMQProperties.retry().backoffPeriod());
        SimpleRetryPolicy policy = new SimpleRetryPolicy();
        policy.setMaxAttempts(rabbitMQProperties.retry().maxAttempts());
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(backOff);
        retryTemplate.setRetryPolicy(policy);
        return retryTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            Jackson2JsonMessageConverter converter,
            RetryTemplate retryTemplate
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(converter);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setAdviceChain(
                RetryInterceptorBuilder
                        .stateless()
                        .retryOperations(retryTemplate)
                        .recoverer(new RejectAndDontRequeueRecoverer())
                        .build()
        );
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public DirectExchange notificationsDirectExchange() {
        return new DirectExchange(rabbitMQProperties.notifications().exchange().direct(), true, false);
    }

    @Bean
    public FanoutExchange notificationsFanoutExchange() {
        return new FanoutExchange(rabbitMQProperties.notifications().exchange().fanout(), true, false);
    }

    @Bean
    public TopicExchange notificationsTopicExchange() {
        return new TopicExchange(rabbitMQProperties.notifications().exchange().topic(), true, false);
    }

    @Bean
    public HeadersExchange notificationsHeadersExchange() {
        return new HeadersExchange(rabbitMQProperties.notifications().exchange().headers(), true, false);
    }

    @Bean
    public DirectExchange notificationsDlxExchange() {
        return new DirectExchange(rabbitMQProperties.notifications().exchange().dlx(), true, false);
    }

    @Bean
    public Queue notificationsDirectQueue() {
        return QueueBuilder.durable(rabbitMQProperties.notifications().queue().general())
                .withArgument("x-dead-letter-exchange", rabbitMQProperties.notifications().exchange().dlx())
                .withArgument("x-dead-letter-routing-key", rabbitMQProperties.notifications().queue().dlx())
                .withArgument("x-message-ttl", rabbitMQProperties.ttl())
                .build();
    }

    @Bean
    public Queue notificationsFanoutQueue() {
        return QueueBuilder.durable(rabbitMQProperties.notifications().queue().audit().fanout())
                .withArgument("x-dead-letter-exchange", rabbitMQProperties.notifications().exchange().dlx())
                .withArgument("x-dead-letter-routing-key", rabbitMQProperties.notifications().queue().dlx())
                .withArgument("x-message-ttl", rabbitMQProperties.ttl())
                .build();
    }

    @Bean
    public Queue notificationsTopicQueue() {
        return QueueBuilder.durable(rabbitMQProperties.notifications().queue().topic())
                .withArgument("x-dead-letter-exchange", rabbitMQProperties.notifications().exchange().dlx())
                .withArgument("x-dead-letter-routing-key", rabbitMQProperties.notifications().queue().dlx())
                .withArgument("x-message-ttl", rabbitMQProperties.ttl())
                .build();
    }

    @Bean
    public Queue notificationsErrorTopicQueue() {
        return QueueBuilder.durable(rabbitMQProperties.notifications().queue().error().topic())
                .withArgument("x-dead-letter-exchange", rabbitMQProperties.notifications().exchange().dlx())
                .withArgument("x-dead-letter-routing-key", rabbitMQProperties.notifications().queue().dlx())
                .withArgument("x-message-ttl", rabbitMQProperties.ttl())
                .build();
    }

    @Bean
    public Queue notificationsHeadersQueue() {
        return QueueBuilder.durable(rabbitMQProperties.notifications().queue().headers())
                .withArgument("x-dead-letter-exchange", rabbitMQProperties.notifications().exchange().dlx())
                .withArgument("x-dead-letter-routing-key", rabbitMQProperties.notifications().queue().dlx())
                .withArgument("x-message-ttl", rabbitMQProperties.ttl())
                .build();
    }

    @Bean
    public Queue notificationsDlxQueue() {
        return QueueBuilder.durable(rabbitMQProperties.notifications().queue().dlx()).build();
    }


    @Bean
    public Binding notificationsDirectBinding(Queue notificationsDirectQueue, DirectExchange notificationsDirectExchange) {
        return BindingBuilder
                .bind(notificationsDirectQueue)
                .to(notificationsDirectExchange)
                .with(rabbitMQProperties.notifications().routingKey().create());
    }

    @Bean
    public Binding notificationsFanoutBinding(Queue notificationsFanoutQueue, FanoutExchange notificationsFanoutExchange) {
        return BindingBuilder
                .bind(notificationsFanoutQueue)
                .to(notificationsFanoutExchange);
    }

    @Bean
    public Binding notificationsTopicBinding(Queue notificationsTopicQueue, TopicExchange notificationsTopicExchange) {
        return BindingBuilder
                .bind(notificationsTopicQueue)
                .to(notificationsTopicExchange)
                .with(rabbitMQProperties.notifications().routingKey().delete());
    }

    @Bean
    public Binding notificationsErrorTopicBinding(Queue notificationsErrorTopicQueue, TopicExchange notificationsTopicExchange) {
        return BindingBuilder
                .bind(notificationsErrorTopicQueue)
                .to(notificationsTopicExchange)
                .with(rabbitMQProperties.notifications().routingKey().error());
    }

    @Bean
    public Binding notificationsHeadersBinding(Queue notificationsHeadersQueue, HeadersExchange notificationsHeadersExchange) {
        return BindingBuilder
                .bind(notificationsHeadersQueue)
                .to(notificationsHeadersExchange)
                .whereAll(Map.of("type", "notification", "source", "taskflow"))
                .match();
    }

    @Bean
    public Binding notificationsDlxBinding(Queue notificationsDlxQueue, DirectExchange notificationsDlxExchange) {
        return BindingBuilder
                .bind(notificationsDlxQueue)
                .to(notificationsDlxExchange)
                .with(rabbitMQProperties.notifications().queue().dlx());
    }

}
