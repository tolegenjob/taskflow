package com.example.TaskFlow.DTO.Request;

import jakarta.validation.constraints.NotBlank;

public record CommentUpdateRequest (

        @NotBlank(message = "Content is required and should not be whitespace")
        String content
) {
}
