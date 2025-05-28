package com.example.TaskFlow.DTO.Response;

import com.example.TaskFlow.Entity.EventLog;
import com.example.TaskFlow.Enum.EntityType;
import com.example.TaskFlow.Enum.EventType;

import java.time.LocalDateTime;
import java.util.Map;

public record EventResponse(
        String id,
        EventType eventType,
        Long entityId,
        EntityType entityType,
        Map<String, Object> payload,
        LocalDateTime createdAt
) { public static EventResponse toDTO (EventLog eventLog) {
        return new EventResponse(
                eventLog.getId().toString(),
                eventLog.getEventType(),
                eventLog.getEntityId(),
                eventLog.getEntityType(),
                eventLog.getPayload(),
                eventLog.getCreatedAt());
    }

}
