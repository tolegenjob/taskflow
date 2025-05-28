package com.example.notificationservice.DTO.Event;

import com.example.notificationservice.Enum.CommentAction;

public record CommentNotificationEvent(
        String content,
        CommentAction action,
        Long taskId,
        Long authorId,
        Long userId
) {
}
