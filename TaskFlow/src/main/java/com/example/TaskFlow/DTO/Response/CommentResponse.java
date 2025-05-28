package com.example.TaskFlow.DTO.Response;

import com.example.TaskFlow.Entity.Comment;

import java.time.LocalDateTime;

public record CommentResponse (

        Long id,
        String content,
        Long taskId,
        Long userId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) { public static CommentResponse toDTO (Comment comment) {
        return new CommentResponse(
            comment.getId(),
            comment.getContent(),
            comment.getTask().getId(),
            comment.getUser().getId(),
            comment.getCreatedAt(),
            comment.getUpdatedAt());
    }
}
