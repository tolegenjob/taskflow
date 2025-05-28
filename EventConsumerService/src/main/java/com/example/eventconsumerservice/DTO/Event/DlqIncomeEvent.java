package com.example.eventconsumerservice.DTO.Event;

import com.example.eventconsumerservice.Enum.EventType;
import com.example.eventconsumerservice.Enum.EntityType;

import java.time.LocalDateTime;
import java.util.Map;

public record DlqIncomeEvent(
        EventType eventType,
        Long entityId,
        EntityType entityType,
        Map<String, Object> payload,
        String errorMessage,
        LocalDateTime failedAt
) {
}
