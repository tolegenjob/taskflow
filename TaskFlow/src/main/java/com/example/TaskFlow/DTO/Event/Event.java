package com.example.TaskFlow.DTO.Event;

import com.example.TaskFlow.Enum.EntityType;
import com.example.TaskFlow.Enum.EventType;

import java.util.Map;

public record Event(
        EventType eventType,
        Long entityId,
        EntityType entityType,
        Map<String, Object> payload
) {

}
