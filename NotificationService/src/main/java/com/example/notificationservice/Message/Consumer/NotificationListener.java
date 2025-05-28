package com.example.notificationservice.Message.Consumer;

import com.example.notificationservice.DTO.Event.CommentNotificationEvent;
import com.example.notificationservice.DTO.Event.NotificationEvent;
import com.example.notificationservice.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationListener{

    private final NotificationService notificationService;

    @RabbitListener(queues = "${spring.rabbitmq.queue.notifications.comment}")
    @Retryable(
            maxAttemptsExpression = "${spring.rabbitmq.retry.max-attempts}",
            backoff = @Backoff(delayExpression = "${spring.rabbitmq.retry.backoff-period}"))
    public void handleDirectCommentNotification(@Payload CommentNotificationEvent commentNotificationEvent) {
        NotificationEvent notificationEvent = new NotificationEvent(
                commentNotificationEvent.taskId(),
                "Notifying author[id %s] of task[id %s]: user[id %s] %s comment[%s] ".formatted(
                        commentNotificationEvent.authorId(),
                        commentNotificationEvent.taskId(),
                        commentNotificationEvent.userId(),
                        commentNotificationEvent.action(),
                        commentNotificationEvent.content()
                ),
                "NOTIFY"
        );
        processEvent(notificationEvent, "direct");
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.notifications.general}")
    @Retryable(
            maxAttemptsExpression = "${spring.rabbitmq.retry.max-attempts}",
            backoff = @Backoff(delayExpression = "${spring.rabbitmq.retry.backoff-period}"))
    public void handleDirectNotification(@Payload NotificationEvent notificationEvent) {
        processEvent(notificationEvent, "direct");
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.notifications.topic}")
    @Retryable(
            maxAttemptsExpression = "${spring.rabbitmq.retry.max-attempts}",
            backoff = @Backoff(delayExpression = "${spring.rabbitmq.retry.backoff-period}"))
    public void handleTopicNotification(@Payload NotificationEvent notificationEvent) {
        processEvent(notificationEvent, "topic");
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.audit.fanout}")
    @Retryable(
            maxAttemptsExpression = "${spring.rabbitmq.retry.max-attempts}",
            backoff = @Backoff(delayExpression = "${spring.rabbitmq.retry.backoff-period}"))
    public void handleFanoutNotification(@Payload NotificationEvent notificationEvent) {
        processEvent(notificationEvent, "fanout");
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.notifications.headers}")
    @Retryable(
            maxAttemptsExpression = "${spring.rabbitmq.retry.max-attempts}",
            backoff = @Backoff(delayExpression = "${spring.rabbitmq.retry.backoff-period}"))
    public void handleHeadersNotification(@Payload NotificationEvent notificationEvent) {
        processEvent(notificationEvent, "headers");
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.dlx.comment}")
    public void handleDlxCommentNotification(@Payload CommentNotificationEvent commentNotificationEvent) {
        try {
            log.warn("Processing CommentNotification message from DLX: {}",
                    commentNotificationEvent);
            NotificationEvent notificationEvent = new NotificationEvent(
                    commentNotificationEvent.taskId(),
                    "Notifying author[id %s] of task[id %s]: user[id %s] added comment[%s] ".formatted(
                            commentNotificationEvent.authorId(),
                            commentNotificationEvent.taskId(),
                            commentNotificationEvent.userId(),
                            commentNotificationEvent.content()
                    ),
                    "NOTIFY"
            );
            notificationService.createNotification(notificationEvent);
        } catch (Exception e) {
            log.error("Failed to process DLX CommentNotification event: {}",
                    commentNotificationEvent,
                    e);
            throw new AmqpRejectAndDontRequeueException("Failed to process DLX notification", e);
        }
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.dlx.notifications}")
    public void handleDlxNotification(@Payload NotificationEvent notificationEvent) {
        try {
            log.warn("Processing message from DLX: {}, {}, {}",
                    notificationEvent.taskId(),
                    notificationEvent.status(),
                    notificationEvent.title());
            notificationService.createNotification(notificationEvent);
        } catch (Exception e) {
            log.error("Failed to process DLX notification event: {}, {}, {}",
                    notificationEvent.taskId(),
                    notificationEvent.status(),
                    notificationEvent.title(),
                    e);
            throw new AmqpRejectAndDontRequeueException("Failed to process DLX notification", e);
        }
    }

    private void processEvent(NotificationEvent notificationEvent,
                              String type) {
        try {
            log.info("Received {} notification event: {}, {}, {}",
                    type,
                    notificationEvent.taskId(),
                    notificationEvent.status(),
                    notificationEvent.title());
            notificationService.createNotification(notificationEvent);
        } catch (Exception e) {
            log.error("Failed to process {} notification event: {}, {}, {}",
                    type,
                    notificationEvent.taskId(),
                    notificationEvent.status(),
                    notificationEvent.title(), e);
            throw new AmqpRejectAndDontRequeueException("Failed to process %s notification %s %s %s"
                    .formatted(type,
                            notificationEvent.taskId(),
                            notificationEvent.status(),
                            notificationEvent.title()),
                    e);
        }
    }

}
