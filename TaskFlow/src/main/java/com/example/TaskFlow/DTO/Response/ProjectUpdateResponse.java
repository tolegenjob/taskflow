package com.example.TaskFlow.DTO.Response;

import com.example.TaskFlow.Entity.Project;
import com.example.TaskFlow.Enum.ProjectStatus;

import java.time.LocalDateTime;

public record ProjectUpdateResponse (

        Long id,
        String name,
        String description,
        ProjectStatus status,
        LocalDateTime updatedAt

) { public static ProjectUpdateResponse toDTO (Project project) {
        return new ProjectUpdateResponse(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getStatus(),
            project.getUpdatedAt());
    }
}
