package com.example.TaskFlow.DTO.Response;

import com.example.TaskFlow.Entity.TaskHistory;
import com.example.TaskFlow.Enum.NotificationStatus;

import java.time.LocalDateTime;
import java.util.Map;

public record TaskHistoryResponse (
        String id,
        Long taskId,
        NotificationStatus action,
        Long performedBy,
        LocalDateTime timestamp,
        Map<String, Object> details
) { public static TaskHistoryResponse toDTO(TaskHistory taskHistory) {
        return new TaskHistoryResponse(
            taskHistory.getId().toString(),
            taskHistory.getTaskId(),
            taskHistory.getAction(),
            taskHistory.getPerformedBy(),
            taskHistory.getTimestamp(),
            taskHistory.getDetails());
    }
}
