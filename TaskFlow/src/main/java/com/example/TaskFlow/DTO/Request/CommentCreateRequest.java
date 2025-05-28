package com.example.TaskFlow.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentCreateRequest (

        @NotBlank(message = "Content is required and should not be whitespace")
        String content,

        @NotNull(message = "User ID is required")
        Long userId
) {
}
