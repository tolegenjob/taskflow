package com.example.eventconsumerservice.Service;

import com.example.eventconsumerservice.Entity.ArchivedProject;
import com.example.eventconsumerservice.Enum.ProjectStatus;
import com.example.eventconsumerservice.Repository.ArchivedProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.example.eventconsumerservice.Util.EntityUtil.findOrThrow;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArchivedProjectService {

    private final ArchivedProjectRepository archivedProjectRepository;

    public void createArchivedProject(Map<String, Object> payload) {
        ArchivedProject archivedProject = new ArchivedProject();
        archivedProject.setProjectId(((Number) payload.get("projectId")).longValue());
        archivedProject.setName((String) payload.get("name"));
        archivedProject.setDescription((String) payload.get("description"));
        archivedProject.setStatus(ProjectStatus.valueOf((String) payload.get("status")));
        archivedProject.setOwnerId(((Number) payload.get("ownerId")).longValue());
        ArchivedProject saved = archivedProjectRepository.save(archivedProject);
        log.info("Created ArchivedProject with id: {}", saved.getId());
    }

    public Page<ArchivedProject> getAllArchivedProjects(Pageable pageable) {
        Page<ArchivedProject> archivedProjects = archivedProjectRepository.findAll(pageable);
        log.info("Got all ArchivedProjects");
        return archivedProjects;
    }

    public ArchivedProject getArchivedProjectById(Long id) {
        ArchivedProject task = findOrThrow(archivedProjectRepository, id, "ArchivedProject");
        log.info("Got ArchivedProject with id: {} ", id);
        return task;
    }

}
