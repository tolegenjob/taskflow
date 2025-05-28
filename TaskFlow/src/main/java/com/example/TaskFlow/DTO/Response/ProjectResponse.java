package com.example.TaskFlow.DTO.Response;

import com.example.TaskFlow.Entity.Project;
import com.example.TaskFlow.Enum.ProjectStatus;

import java.time.LocalDateTime;

public record ProjectResponse (

        Long id,
        String name,
        String description,
        ProjectStatus status,
        Long ownerId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) { public static ProjectResponse toDTO (Project project) {
        return new ProjectResponse(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getStatus(),
            project.getOwner().getId(),
            project.getCreatedAt(),
            project.getUpdatedAt());
    }
}
