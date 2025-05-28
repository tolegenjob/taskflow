package com.example.TaskFlow.DTO.Response;

import com.example.TaskFlow.Entity.Comment;

import java.time.LocalDateTime;

public record CommentUpdateResponse (

        Long id,
        String content,
        LocalDateTime updatedAt

) { public static CommentUpdateResponse toDTO (Comment comment) {
        return new CommentUpdateResponse(
            comment.getId(),
            comment.getContent(),
            comment.getUpdatedAt());
    }
}
