package com.example.TaskFlow.DTO.Event;

public record NotificationEvent(
        Long entityId,
        String title,
        String status
) {
}
