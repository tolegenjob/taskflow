package com.example.TaskFlow.DTO.Response;

import com.example.TaskFlow.Entity.Task;
import com.example.TaskFlow.Enum.TaskPriority;
import com.example.TaskFlow.Enum.TaskStatus;

import java.time.LocalDateTime;

public record TaskCreateResponse (
        Long id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        LocalDateTime deadline,
        Long assignedUserId,
        Long projectId,
        LocalDateTime createdAt
) { public static TaskCreateResponse toDTO (Task task) {
        return new TaskCreateResponse(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getPriority(),
            task.getDeadline(),
            task.getAssignedUser().getId(),
            task.getProject().getId(),
            task.getCreatedAt());
    }
}
