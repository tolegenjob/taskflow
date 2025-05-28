package com.example.notificationservice.DTO.Event;

import com.example.notificationservice.Enum.LogLevel;

import java.util.Map;

public record LogEntryEvent(
    LogLevel level,
    String message,
    Map<String, Object> context
) {
}
