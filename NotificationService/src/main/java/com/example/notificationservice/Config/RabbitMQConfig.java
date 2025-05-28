package com.example.notificationservice.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
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
    public DirectExchange directExchange() {
        return new DirectExchange(rabbitMQProperties.exchange().direct(), true, false);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(rabbitMQProperties.exchange().fanout(), true, false);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(rabbitMQProperties.exchange().topic(), true, false);
    }

    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange(rabbitMQProperties.exchange().headers(), true, false);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(rabbitMQProperties.exchange().dlx(), true, false);
    }

    @Bean
    public Queue directQueue() {
        return QueueBuilder.durable(rabbitMQProperties.queue().notifications().general())
                .withArgument("x-dead-letter-exchange", rabbitMQProperties.exchange().dlx())
                .withArgument("x-dead-letter-routing-key", rabbitMQProperties.queue().dlx().notifications())
                .withArgument("x-message-ttl", rabbitMQProperties.queue().ttl())
                .build();
    }

    @Bean
    public Queue directCommentQueue() {
        return QueueBuilder.durable(rabbitMQProperties.queue().notifications().comment())
                .withArgument("x-dead-letter-exchange", rabbitMQProperties.exchange().dlx())
                .withArgument("x-dead-letter-routing-key", rabbitMQProperties.queue().dlx().comment())
                .withArgument("x-message-ttl", rabbitMQProperties.queue().ttl())
                .build();
    }

    @Bean
    public Queue fanoutQueue() {
        return QueueBuilder.durable(rabbitMQProperties.queue().audit().fanout())
                .withArgument("x-dead-letter-exchange", rabbitMQProperties.exchange().dlx())
                .withArgument("x-dead-letter-routing-key", rabbitMQProperties.queue().dlx().notifications())
                .withArgument("x-message-ttl", rabbitMQProperties.queue().ttl())
                .build();
    }

    @Bean
    public Queue topicQueue() {
        return QueueBuilder.durable(rabbitMQProperties.queue().notifications().topic())
                .withArgument("x-dead-letter-exchange", rabbitMQProperties.exchange().dlx())
                .withArgument("x-dead-letter-routing-key", rabbitMQProperties.queue().dlx().notifications())
                .withArgument("x-message-ttl", rabbitMQProperties.queue().ttl())
                .build();
    }

    @Bean
    public Queue errorTopicQueue() {
        return QueueBuilder.durable(rabbitMQProperties.queue().error().topic())
                .withArgument("x-dead-letter-exchange", rabbitMQProperties.exchange().dlx())
                .withArgument("x-dead-letter-routing-key", rabbitMQProperties.queue().dlx().notifications())
                .withArgument("x-message-ttl", rabbitMQProperties.queue().ttl())
                .build();
    }

    @Bean
    public Queue headersQueue() {
        return QueueBuilder.durable(rabbitMQProperties.queue().notifications().headers())
                .withArgument("x-dead-letter-exchange", rabbitMQProperties.exchange().dlx())
                .withArgument("x-dead-letter-routing-key", rabbitMQProperties.queue().dlx().notifications())
                .withArgument("x-message-ttl", rabbitMQProperties.queue().ttl())
                .build();
    }

    @Bean
    public Queue dlxQueue() {
        return QueueBuilder.durable(rabbitMQProperties.queue().dlx().notifications()).build();
    }

    @Bean
    public Queue dlxCommentQueue() {
        return QueueBuilder.durable(rabbitMQProperties.queue().dlx().comment()).build();
    }


    @Bean
    public Binding directBinding(Queue directQueue, DirectExchange directExchange) {
        return BindingBuilder
                .bind(directQueue)
                .to(directExchange)
                .with(rabbitMQProperties.routingKey().direct());
    }

    @Bean
    public Binding directCommentBinding(Queue directCommentQueue, DirectExchange directExchange) {
        return BindingBuilder
                .bind(directCommentQueue)
                .to(directExchange)
                .with(rabbitMQProperties.routingKey().comment());
    }

    @Bean
    public Binding fanoutBinding(Queue fanoutQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder
                .bind(fanoutQueue)
                .to(fanoutExchange);
    }

    @Bean
    public Binding topicBinding(Queue topicQueue, TopicExchange topicExchange) {
        return BindingBuilder
                .bind(topicQueue)
                .to(topicExchange)
                .with(rabbitMQProperties.routingKey().topic().general());
    }

    @Bean
    public Binding errorTopicBinding(Queue errorTopicQueue, TopicExchange topicExchange) {
        return BindingBuilder
                .bind(errorTopicQueue)
                .to(topicExchange)
                .with(rabbitMQProperties.routingKey().topic().error());
    }

    @Bean
    public Binding headersBinding(Queue headersQueue, HeadersExchange headersExchange) {
        return BindingBuilder
                .bind(headersQueue)
                .to(headersExchange)
                .whereAll(Map.of("type", "notification", "source", "taskflow"))
                .match();
    }

    @Bean
    public Binding dlxBinding(Queue dlxQueue, DirectExchange dlxExchange) {
        return BindingBuilder
                .bind(dlxQueue)
                .to(dlxExchange)
                .with(rabbitMQProperties.queue().dlx().notifications());
    }

    @Bean
    public RetryTemplate retryTemplate() {
        FixedBackOffPolicy backOff = new FixedBackOffPolicy();
        backOff.setBackOffPeriod(rabbitMQProperties.retry().backoffPeriod());

        SimpleRetryPolicy policy = new SimpleRetryPolicy();
        policy.setMaxAttempts(rabbitMQProperties.retry().maxAttempts());

        RetryTemplate template = new RetryTemplate();
        template.setBackOffPolicy(backOff);
        template.setRetryPolicy(policy);
        return template;
    }


    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf,
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            Jackson2JsonMessageConverter converter,
            RetryTemplate retryTemplate
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, cf);
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
    public RabbitAdmin rabbitAdmin(ConnectionFactory cf) {
        RabbitAdmin admin = new RabbitAdmin(cf);
        admin.setIgnoreDeclarationExceptions(true);
        return admin;
    }


}
