package com.example.TaskFlow.DTO.Response;

import com.example.TaskFlow.Entity.Project;
import com.example.TaskFlow.Enum.ProjectStatus;

import java.time.LocalDateTime;

public record ProjectCreateResponse (

        Long id,
        String name,
        String description,
        ProjectStatus status,
        Long ownerId,
        LocalDateTime createdAt

) { public static ProjectCreateResponse toDTO (Project project) {
        return new ProjectCreateResponse(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getStatus(),
            project.getOwner().getId(),
            project.getCreatedAt());
    }
}
