package com.example.TaskFlow.DTO.Request;

import com.example.TaskFlow.Enum.TaskPriority;
import com.example.TaskFlow.Enum.TaskStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TaskUpdateRequest(

        @NotBlank(message = "Title is required and should not be whitespace")
        String title,

        @NotBlank(message = "Description is required and should not be whitespace")
        String description,

        @NotNull(message = "Status is required")
        TaskStatus status,

        @NotNull(message = "Priority is required")
        TaskPriority priority,

        @Future(message = "Deadline should not be present or past")
        LocalDateTime deadline,

        @NotNull(message = "User ID is required")
        Long assignedUserId
) {
}
