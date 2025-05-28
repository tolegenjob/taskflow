package com.example.TaskFlow.DTO.Response;

import com.example.TaskFlow.Entity.Comment;

import java.time.LocalDateTime;

public record CommentCreateResponse (

        Long id,
        String content,
        Long taskId,
        Long userId,
        LocalDateTime createdAt

) { public static CommentCreateResponse toDTO (Comment comment) {
        return new CommentCreateResponse(
            comment.getId(),
            comment.getContent(),
            comment.getTask().getId(),
            comment.getUser().getId(),
            comment.getCreatedAt());
    }
}
