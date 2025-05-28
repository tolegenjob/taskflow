package com.example.TaskFlow.DTO.Event;

import com.example.TaskFlow.Enum.NotificationStatus;

public record TaskHistoryNotificationEvent(
        Long taskId,
        String title,
        NotificationStatus status
) {
}
