package com.example.eventconsumerservice.DTO.Response;

import com.example.eventconsumerservice.Entity.ArchivedProject;
import com.example.eventconsumerservice.Enum.ProjectStatus;

public record ArchivedProjectResponse(
        Long id,
        Long projectId,
        String name,
        String description,
        ProjectStatus projectStatus,
        Long ownerId
) { public static ArchivedProjectResponse toDTO(ArchivedProject archivedProject) {
        return new ArchivedProjectResponse(
                archivedProject.getId(),
                archivedProject.getProjectId(),
                archivedProject.getName(),
                archivedProject.getDescription(),
                archivedProject.getStatus(),
                archivedProject.getOwnerId()
        );
    }
}
