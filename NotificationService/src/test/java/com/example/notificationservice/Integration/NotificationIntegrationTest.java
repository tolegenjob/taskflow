package com.example.notificationservice.Integration;

import com.example.notificationservice.DTO.Event.NotificationEvent;
import com.example.notificationservice.Entity.Notification;
import com.example.notificationservice.Repository.NotificationRepository;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NotificationIntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @Value("${rabbitmq.exchange.direct}")
    private String exchange;

    @Value("${rabbitmq.routing-key.direct}")
    private String routingKey;

    @BeforeEach
    void cleanDatabase() {
        notificationRepository.deleteAll();
    }

    @Test
    void receiveAndSaveNotification_success() {
        NotificationEvent event = new NotificationEvent(
                1L,
                "task-created",
                "CREATE"
        );
        rabbitTemplate.convertAndSend(exchange, routingKey, event);

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    List<Notification> notifications = notificationRepository.findAll();
                    assertThat(notifications).hasSize(1);
                    Notification saved = notifications.getFirst();
                    assertThat(saved.getTitle()).isEqualTo("task-created");
                    assertThat(saved.getStatus()).isEqualTo("CREATE");
                });
    }
}
