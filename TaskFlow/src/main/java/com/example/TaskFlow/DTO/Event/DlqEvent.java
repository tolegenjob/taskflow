package com.example.TaskFlow.DTO.Event;

import com.example.TaskFlow.Enum.EntityType;
import com.example.TaskFlow.Enum.EventType;

import java.time.LocalDateTime;
import java.util.Map;

public record DlqEvent(
        EventType eventType,
        Long entityId,
        EntityType entityType,
        Map<String, Object> payload,
        String errorMessage,
        LocalDateTime failedAt
) {
}
