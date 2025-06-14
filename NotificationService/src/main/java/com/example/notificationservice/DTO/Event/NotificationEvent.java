package com.example.notificationservice.DTO.Event;

public record NotificationEvent(
        Long entityId,
        String title,
        String status
) {
}

