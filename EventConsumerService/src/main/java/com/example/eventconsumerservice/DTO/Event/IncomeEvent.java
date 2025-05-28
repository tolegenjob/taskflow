package com.example.eventconsumerservice.DTO.Event;

import com.example.eventconsumerservice.Enum.EntityType;
import com.example.eventconsumerservice.Enum.EventType;

import java.util.Map;

public record IncomeEvent(
        EventType eventType,
        Long entityId,
        EntityType entityType,
        Map<String, Object> payload
) {

}
