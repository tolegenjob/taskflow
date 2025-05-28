package com.example.TaskFlow.DTO.Response;

import com.example.TaskFlow.Entity.Task;
import com.example.TaskFlow.Enum.TaskPriority;
import com.example.TaskFlow.Enum.TaskStatus;

import java.time.LocalDateTime;

public record TaskUpdateResponse (
        Long id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        LocalDateTime deadline,
        Long assignedUserId ,
        LocalDateTime updatedAt
) { public static TaskUpdateResponse toDTO (Task task) {
        return new TaskUpdateResponse(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getPriority(),
            task.getDeadline(),
            task.getAssignedUser().getId() ,
            task.getUpdatedAt());
    }
}
