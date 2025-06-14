package com.example.TaskFlow.Service;

import com.example.TaskFlow.Config.KafkaProperties;
import com.example.TaskFlow.DTO.Request.ProjectCreateRequest;
import com.example.TaskFlow.DTO.Request.ProjectUpdateRequest;
import com.example.TaskFlow.Entity.Project;
import com.example.TaskFlow.Enum.EntityType;
import com.example.TaskFlow.Enum.EventType;
import com.example.TaskFlow.Enum.LogLevel;
import com.example.TaskFlow.Enum.ProjectStatus;
import com.example.TaskFlow.Message.Producer.EventLogProducer;
import com.example.TaskFlow.Message.Producer.LogEntryEventProducer;
import com.example.TaskFlow.Message.Producer.RedisMessageProducer;
import com.example.TaskFlow.Repository.ProjectRepository;
import com.example.TaskFlow.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.example.TaskFlow.Enum.EventType.PROJECT_UPDATED;
import static com.example.TaskFlow.Util.EntityUtil.findOrThrow;
import static com.example.TaskFlow.Util.MessageUtil.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final LogEntryEventProducer logEntryEventProducer;
    private final KafkaProperties kafkaProperties;
    private final EventLogProducer eventLogProducer;
    private final RedisMessageProducer redisMessageProducer;

    @CachePut(value = "projects", key = "#result.id")
    public Project createProject(ProjectCreateRequest projectCreateRequest) {
        Project project = new Project();
        project.setName(projectCreateRequest.name());
        project.setDescription(projectCreateRequest.description());
        project.setStatus(projectCreateRequest.status());
        project.setOwner(findOrThrow(userRepository, projectCreateRequest.ownerId(), "User"));
        Project saved = projectRepository.save(project);
        log.info("Created project with id: {}", saved.getId());
        Map<String, Object> context = getProjectDetails(saved);
        sendLogEntryEventToKafka(
                logEntryEventProducer,
                kafkaProperties.topic().logs(),
                LogLevel.INFO,
                "Created project with id: %s".formatted(saved.getId()),
                context);
        sendEventLogToKafka(eventLogProducer,
                kafkaProperties.topic().events(),
                EventType.PROJECT_CREATED,
                project.getId(),
                EntityType.PROJECT,
                context);
        return saved;
    }

    public Page<Project> getAllProjects(Pageable pageable) {
        Page<Project> projects = projectRepository.findAll(pageable);
        log.info("Got all projects");
        return projects;
    }

    @Cacheable(value = "projects", key = "#id")
    public Project getProjectById(Long id) {
        Project project = findOrThrow(projectRepository, id, "Project");
        log.info("Got project with id: {} ", id);
        return project;
    }

    @CachePut(value = "projects", key = "#result.id")
    public Project updateProjectById(Long id, ProjectUpdateRequest projectUpdateRequest) {
        Project project = findOrThrow(projectRepository, id, "Project");
        project.setName(projectUpdateRequest.name());
        project.setDescription(projectUpdateRequest.description());
        project.setStatus(projectUpdateRequest.status());
        Project saved = projectRepository.save(project);
        log.info("Updated project with id: {} ", id);
        Map<String, Object> context = getProjectDetails(saved);
        sendLogEntryEventToKafka(
                logEntryEventProducer,
                kafkaProperties.topic().logs(),
                LogLevel.INFO,
                "Updated project with id: %s".formatted(saved.getId()),
                context);
        EventType eventType;
        if(projectUpdateRequest.status().equals(ProjectStatus.ARCHIVED))
        {
            eventType = EventType.PROJECT_ARCHIVED;
        } else {
            eventType = PROJECT_UPDATED;
        }
        sendEventLogToKafka(eventLogProducer,
                kafkaProperties.topic().events(),
                eventType,
                project.getId(),
                EntityType.PROJECT,
                context);
        sendNotificationEventToRedis(
                redisMessageProducer,
                saved.getId(),
                saved.getName(),
                "PROJECT_UPDATED");
        return saved;
    }

    @CacheEvict(value = "projects", key = "#id")
    public void deleteProjectById(Long id) {
        Project project = findOrThrow(projectRepository, id, "Project");
        projectRepository.delete(project);
        log.info("Deleted project with id: {} ", id);
        Map<String, Object> context = getProjectDetails(project);
        sendLogEntryEventToKafka(
                logEntryEventProducer,
                kafkaProperties.topic().logs(),
                LogLevel.INFO,
                "Deleted project with id: %s".formatted(project.getId()),
                context);
        sendEventLogToKafka(eventLogProducer,
                kafkaProperties.topic().events(),
                EventType.PROJECT_DELETED,
                project.getId(),
                EntityType.PROJECT,
                context);
        sendNotificationEventToRedis(
                redisMessageProducer,
                project.getId(),
                project.getName(),
                "PROJECT DELETED");
    }

    private Map<String, Object> getProjectDetails(Project project) {
        return Map.of(
                "id", project.getId(),
                "projectId", project.getId(),
                "name", project.getName(),
                "description", project.getDescription(),
                "status", project.getStatus(),
                "ownerId", project.getOwner().getId()
        );
    }

}
