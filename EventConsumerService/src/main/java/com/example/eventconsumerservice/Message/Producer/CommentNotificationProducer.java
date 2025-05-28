package com.example.eventconsumerservice.Message.Producer;

import com.example.eventconsumerservice.DTO.Event.CommentNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentNotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendCommentNotificationEvent(String exchange, String routingKey, CommentNotificationEvent event) {
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
        log.info("Sending CommentNotificationEvent: [{}, {}, {}, {}] with exchange [{}], routing-key [{}]",
                event.content(),
                event.taskId(),
                event.authorId(),
                event.userId(),
                exchange,
                routingKey);
    }

}
