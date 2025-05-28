package com.example.eventconsumerservice.DTO.Event;

import com.example.eventconsumerservice.Enum.LogLevel;

import java.util.Map;

public record LogEntryEvent(
    LogLevel level,
    String message,
    Map<String, Object> context
) {
}
