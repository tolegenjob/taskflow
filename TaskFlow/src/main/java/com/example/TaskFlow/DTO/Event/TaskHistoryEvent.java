package com.example.TaskFlow.DTO.Event;

import com.example.TaskFlow.Enum.NotificationStatus;

import java.util.Map;

public record TaskHistoryEvent(
        Long taskId,
        NotificationStatus action,
        Long performedBy,
        Map<String, Object> details
) {
}
