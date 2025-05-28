package com.example.notificationservice.Service;

import com.example.notificationservice.DTO.Event.NotificationEvent;
import com.example.notificationservice.Entity.Notification;
import com.example.notificationservice.Repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.example.notificationservice.Util.EntityUtil.findOrThrow;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void createNotification(NotificationEvent notificationEvent) {
        if (notificationEvent.taskId() == null) {
            throw new IllegalArgumentException("Task id is required");
        }
        Notification notification = new Notification();
        notification.setTaskId(notificationEvent.taskId());
        notification.setTitle(notificationEvent.title());
        notification.setStatus(notificationEvent.status());
        Notification saved  = notificationRepository.save(notification);
        log.info("Created Notification with id: {}", saved.getId());
    }

    public Page<Notification> getAllNotifications(Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findAll(pageable);
        log.info("Got all Notifications");
        return notifications;
    }

    public Notification getNotificationById(Long id) {
        Notification notification = findOrThrow(notificationRepository, id, "Notification");
        log.info("Got Notification with id: {} ", id);
        return notification;
    }

}
