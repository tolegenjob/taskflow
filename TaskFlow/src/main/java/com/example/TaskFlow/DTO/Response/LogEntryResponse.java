package com.example.TaskFlow.DTO.Response;

import com.example.TaskFlow.Entity.LogEntry;

import java.time.LocalDateTime;
import java.util.Map;

public record LogEntryResponse (
        String id,
        String level,
        String message,
        LocalDateTime timestamp,
        Map<String, Object> context
) { public static LogEntryResponse toDTO(LogEntry logEntry) {
        return new LogEntryResponse(
            logEntry.getId().toString(),
            logEntry.getLevel().toString(),
            logEntry.getMessage(),
            logEntry.getTimestamp(),
            logEntry.getContext());
    }

}
