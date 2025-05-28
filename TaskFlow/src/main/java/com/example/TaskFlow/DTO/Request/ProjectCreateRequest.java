package com.example.TaskFlow.DTO.Request;

import com.example.TaskFlow.Enum.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProjectCreateRequest (

        @NotBlank(message = "Name is required and should not be whitespace")
        String name,

        @NotBlank(message = "Description is required and should not be whitespace")
        String description,

        @NotNull(message = "Status is required")
        ProjectStatus status,

        @NotNull(message = "Owner ID is required")
        Long ownerId
) {
}
