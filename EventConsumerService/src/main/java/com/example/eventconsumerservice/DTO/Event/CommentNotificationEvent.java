package com.example.eventconsumerservice.DTO.Event;

import com.example.eventconsumerservice.Enum.CommentAction;

public record CommentNotificationEvent(
        String content,
        CommentAction action,
        Long taskId,
        Long authorId,
        Long userId
) {
}

