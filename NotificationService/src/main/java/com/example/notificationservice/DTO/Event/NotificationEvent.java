package com.example.notificationservice.DTO.Event;

public record NotificationEvent(
        Long taskId,
        String title,
        String status
) {
}

