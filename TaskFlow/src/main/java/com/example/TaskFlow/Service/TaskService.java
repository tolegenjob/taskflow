package com.example.TaskFlow.Service;

import com.example.TaskFlow.Config.KafkaProperties;
import com.example.TaskFlow.Config.RabbitMQProperties;
import com.example.TaskFlow.DTO.Event.TaskHistoryEvent;
import com.example.TaskFlow.DTO.Event.TaskHistoryNotificationEvent;
import com.example.TaskFlow.DTO.Request.TaskCreateRequest;
import com.example.TaskFlow.DTO.Request.TaskUpdateRequest;
import com.example.TaskFlow.Entity.Task;
import com.example.TaskFlow.Enum.*;
import com.example.TaskFlow.Message.Producer.EventLogProducer;
import com.example.TaskFlow.Message.Producer.LogEntryEventProducer;
import com.example.TaskFlow.Message.Producer.TaskHistoryEventProducer;
import com.example.TaskFlow.Repository.ProjectRepository;
import com.example.TaskFlow.Repository.TaskRepository;
import com.example.TaskFlow.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static com.example.TaskFlow.Util.EntityUtil.findOrThrow;
import static com.example.TaskFlow.Util.MessageUtil.sendEventLogToKafka;
import static com.example.TaskFlow.Util.MessageUtil.sendLogEntryEventToKafka;


@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskHistoryEventProducer taskHistoryEventProducer;
    private final RabbitMQProperties rabbitMQProperties;
    private final LogEntryEventProducer logEntryEventProducer;
    private final KafkaProperties kafkaProperties;
    private final EventLogProducer eventLogProducer;

    @CachePut(value = "tasks", key = "#result.id")
    public Task createTask(TaskCreateRequest taskCreateRequest) {
        Task task = new Task();
        setTaskProperties(task,
                taskCreateRequest.title(),
                taskCreateRequest.description(),
                taskCreateRequest.status(),
                taskCreateRequest.priority(),
                taskCreateRequest.deadline());
        task.setAssignedUser(findOrThrow(userRepository, taskCreateRequest.assignedUserId(), "User"));
        task.setProject(findOrThrow(projectRepository, taskCreateRequest.projectId(), "Project"));
        Task saved = taskRepository.save(task);
        log.info("Created task with id: {}", saved.getId());
        Map<String, Object> context = getTaskDetails(saved);
        sendTaskHistoryEvent(task, NotificationStatus.CREATE, context);
        sendLogEntryEventToKafka(
                logEntryEventProducer,
                kafkaProperties.topic().logs(),
                LogLevel.INFO,
                "Created task with id: %s".formatted(saved.getId()),
                context);
        sendEventLogToKafka(eventLogProducer,
                kafkaProperties.topic().events(),
                EventType.TASK_CREATED,
                task.getId(),
                EntityType.TASK,
                context);
        return saved;
    }

    public Page<Task> getAllTasks(Pageable pageable) {
        Page<Task> tasks = taskRepository.findAll(pageable);
        log.info("Got all tasks");
        return tasks;
    }

    @Cacheable(value = "tasks", key = "#id")
    public Task getTaskById(Long id) {
        Task task = findOrThrow(taskRepository, id, "Task");
        log.info("Got task with id: {} ", id);
        return task;
    }

    @CachePut(value = "tasks", key = "#result.id")
    public Task updateTaskById(Long id, TaskUpdateRequest taskUpdateRequest) {
        Task task = findOrThrow(taskRepository, id, "Task");
        setTaskProperties(task,
                taskUpdateRequest.title(),
                taskUpdateRequest.description(),
                taskUpdateRequest.status(),
                taskUpdateRequest.priority(),
                taskUpdateRequest.deadline());
        task.setAssignedUser(findOrThrow(userRepository, taskUpdateRequest.assignedUserId(), "User"));
        Task saved = taskRepository.save(task);
        log.info("Updated task with id: {} ", id);
        Map<String, Object> context = getTaskDetails(saved);
        sendTaskHistoryEvent(task, NotificationStatus.UPDATE, context);
        sendLogEntryEventToKafka(
                logEntryEventProducer,
                kafkaProperties.topic().logs(),
                LogLevel.INFO,
                "Updated task with id: %s".formatted(saved.getId()),
                context);
        sendEventLogToKafka(eventLogProducer,
                kafkaProperties.topic().events(),
                EventType.TASK_UPDATED,
                task.getId(),
                EntityType.TASK,
                context);
        return saved;
    }

    @CacheEvict(value = "tasks", key = "#id")
    public void deleteTaskById(Long id) {
        Task task = findOrThrow(taskRepository, id, "Task");
        taskRepository.delete(task);
        log.info("Deleted task with id: {} ", id);
        Map<String, Object> context = getTaskDetails(task);
        sendTaskHistoryEvent(task, NotificationStatus.DELETE, context);
        sendLogEntryEventToKafka(
                logEntryEventProducer,
                kafkaProperties.topic().logs(),
                LogLevel.INFO,
                "Deleted task with id: %s".formatted(task.getId()),
                context);
        sendEventLogToKafka(eventLogProducer,
                kafkaProperties.topic().events(),
                EventType.TASK_DELETED,
                task.getId(),
                EntityType.TASK,
                context);
    }

    public Page<Task> filterTasksByStatusAndPriority(String status, String priority, Pageable pageable) {
        TaskStatus taskStatus = TaskStatus.valueOf(status);
        TaskPriority taskPriority = TaskPriority.valueOf(priority);
        return taskRepository.findByStatusAndPriority(taskStatus, taskPriority, pageable);
    }

    private void setTaskProperties(Task task,
                                   String title,
                                   String description,
                                   TaskStatus status,
                                   TaskPriority priority,
                                   LocalDateTime deadline) {
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setPriority(priority);
        task.setDeadline(deadline);
    }

    private void sendTaskHistoryEvent(Task task, NotificationStatus action, Map<String, Object> details) {

        TaskHistoryEvent taskHistoryEvent = new TaskHistoryEvent(
                task.getId(),
                action,
                task.getAssignedUser().getId(),
                details
        );

        TaskHistoryNotificationEvent taskHistoryNotificationEvent = new TaskHistoryNotificationEvent(
                task.getId(),
                task.getTitle(),
                action
        );

        taskHistoryEventProducer.sendTaskHistoryEvent(
                rabbitMQProperties.history().exchange(),
                rabbitMQProperties.history().routingKey(),
                taskHistoryEvent);

        String exchange = switch (action) {
            case CREATE -> rabbitMQProperties.notifications().exchange().direct();
            case UPDATE -> rabbitMQProperties.notifications().exchange().fanout();
            case DELETE -> rabbitMQProperties.notifications().exchange().topic();
        };

        String routingKey = switch (action) {
            case CREATE -> rabbitMQProperties.notifications().routingKey().create();
            case UPDATE -> "";
            case DELETE -> rabbitMQProperties.notifications().routingKey().delete();
        };

        taskHistoryEventProducer.sendTaskHistoryNotificationEvent(
                exchange,
                routingKey,
                taskHistoryNotificationEvent);
    }

    private Map<String, Object> getTaskDetails(Task task) {
        return Map.of(
                "id", task.getId(),
                "title", task.getTitle(),
                "description", task.getDescription(),
                "status", task.getStatus(),
                "priority", task.getPriority(),
                "deadline", task.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")),
                "assignedUserId", task.getAssignedUser().getId(),
                "projectId", task.getProject().getId()
        );
    }

}
