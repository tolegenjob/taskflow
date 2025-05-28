package com.example.notificationservice.DTO.Response;

import com.example.notificationservice.Entity.Notification;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        Long taskId,
        String title,
        String status,
        LocalDateTime timestamp
) { public static NotificationResponse toDTO(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getTaskId(),
                notification.getTitle(),
                notification.getStatus(),
                notification.getTimestamp());
    }
}
