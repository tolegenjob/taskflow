package com.example.TaskFlow.DTO.Event;

import com.example.TaskFlow.Enum.LogLevel;

import java.util.Map;

public record LogEntryEvent(
    LogLevel level,
    String message,
    Map<String, Object> context
) {
}
