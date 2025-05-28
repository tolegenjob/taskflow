package com.example.eventconsumerservice.DTO.Response;

import com.example.eventconsumerservice.Entity.DlqEvent;
import com.example.eventconsumerservice.Enum.EntityType;
import com.example.eventconsumerservice.Enum.EventType;

import java.time.LocalDateTime;
import java.util.Map;

public record DlqEventResponse(
        String id,
        EventType eventType,
        Long entityId,
        EntityType entityType,
        Map<String, Object> payload,
        String errorMessage,
        LocalDateTime failedAt
) { public static DlqEventResponse toDTO(DlqEvent dlqEvent) {
        return new DlqEventResponse(
                dlqEvent.getId().toString(),
                dlqEvent.getEventType(),
                dlqEvent.getEntityId(),
                dlqEvent.getEntityType(),
                dlqEvent.getPayload(),
                dlqEvent.getErrorMessage(),
                dlqEvent.getFailedAt()
        );
    }
}
