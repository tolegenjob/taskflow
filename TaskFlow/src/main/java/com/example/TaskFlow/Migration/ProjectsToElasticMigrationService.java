package com.example.TaskFlow.Migration;

import com.example.TaskFlow.Entity.Project;
import com.example.TaskFlow.Index.ProjectIndex;
import com.example.TaskFlow.Repository.ProjectIndexRepository;
import com.example.TaskFlow.Repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectsToElasticMigrationService {

    private final ProjectRepository projectRepository;
    private final ProjectIndexRepository projectIndexRepository;

    @Transactional(readOnly = true)
    public void migrateAllCommentsToElasticsearch() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectIndex> projectIndicesToSave = projects.stream()
                .filter(project -> !projectIndexRepository.existsById(project.getId()))
                .map(project -> {
                    ProjectIndex projectIndex = new ProjectIndex();
                    setProperties(project, projectIndex);
                    return projectIndex;
                })
                .toList();
        if (!projectIndicesToSave.isEmpty()) {
            projectIndexRepository.saveAll(projectIndicesToSave);
        }
    }

    private void setProperties(Project project, ProjectIndex projectIndex) {
        projectIndex.setId(project.getId());
        projectIndex.setName(project.getName());
        projectIndex.setDescription(project.getDescription());
        projectIndex.setStatus(project.getStatus());
    }
}
