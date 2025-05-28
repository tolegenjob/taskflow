package com.example.eventconsumerservice.DTO.Response;

import com.example.eventconsumerservice.Entity.ProcessedEvent;
import com.example.eventconsumerservice.Enum.EntityType;
import com.example.eventconsumerservice.Enum.EventType;

import java.time.LocalDateTime;
import java.util.Map;

public record ProcessedEventResponse(
        String id,
        EventType eventType,
        Long entityId,
        EntityType entityType,
        Map<String, Object> payload,
        LocalDateTime createdAt
) { public static ProcessedEventResponse toDTO(ProcessedEvent processedEvent) {
        return new ProcessedEventResponse(
                processedEvent.getId().toString(),
                processedEvent.getEventType(),
                processedEvent.getEntityId(),
                processedEvent.getEntityType(),
                processedEvent.getPayload(),
                processedEvent.getCreatedAt()
        );
    }
}
