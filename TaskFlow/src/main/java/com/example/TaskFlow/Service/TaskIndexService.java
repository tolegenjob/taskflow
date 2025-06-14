package com.example.TaskFlow.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.TaskFlow.DTO.Event.Event;
import com.example.TaskFlow.Enum.TaskPriority;
import com.example.TaskFlow.Enum.TaskStatus;
import com.example.TaskFlow.Index.TaskIndex;
import com.example.TaskFlow.Repository.TaskIndexRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.TaskFlow.Util.EntityUtil.findOrThrow;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskIndexService {

    private final TaskIndexRepository taskIndexRepository;
    private final ElasticsearchClient elasticsearchClient;

    public Page<TaskIndex> processSearch(
            String query,
            TaskStatus status,
            TaskPriority priority,
            Pageable pageable) {

        if (status != null && priority != null) {
            return taskIndexRepository.searchByQueryAndStatusAndPriority(query, status.toString(), priority.toString(), pageable);
        } else if (status != null) {
            return taskIndexRepository.searchByQueryAndStatus(query, status.toString(), pageable);
        } else if (priority != null) {
            return taskIndexRepository.searchByQueryAndPriority(query, priority.toString(), pageable);
        } else {
            return taskIndexRepository.searchByQuery(query, pageable);
        }

    }

    public Map<String, Long> getTaskCountByStatus() {
        try {
            SearchResponse<Void> response = elasticsearchClient.search(s -> s
                            .index("tasks")
                            .size(0)
                            .aggregations("by_status", a -> a
                                    .terms(t -> t.field("status"))
                            ),
                    Void.class
            );
            log.info("Got count of tasks by status");
            return response.aggregations()
                    .get("by_status")
                    .sterms()
                    .buckets()
                    .array()
                    .stream()
                    .collect(Collectors.toMap(
                            bucket -> bucket.key().stringValue(),
                            bucket -> bucket.docCount()
                    ));

        } catch (IOException e) {
            log.error("Error aggregating task count by status: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    public Map<String, Long> getTaskCountByPriority() {
        try {
            SearchResponse<Void> response = elasticsearchClient.search(s -> s
                            .index("tasks")
                            .size(0)
                            .aggregations("by_priority", a -> a
                                    .terms(t -> t.field("priority"))
                            ),
                    Void.class
            );
            log.info("Got count of tasks by priority");
            return response.aggregations()
                    .get("by_priority")
                    .sterms()
                    .buckets()
                    .array()
                    .stream()
                    .collect(Collectors.toMap(
                            bucket -> bucket.key().stringValue(),
                            bucket -> bucket.docCount()
                    ));

        } catch (IOException e) {
            log.error("Error aggregating task count by priority: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    public void createTaskIndex(Event event) {
        TaskIndex taskIndex = new TaskIndex();
        setProperties(event, taskIndex);
        taskIndexRepository.save(taskIndex);
        log.info("Task index created: {}", taskIndex);
    }

    public void updateTaskIndex(Event event) {
        Long id = ((Number) event.payload().get("id")).longValue();
        TaskIndex taskIndex = findOrThrow(taskIndexRepository, id, "TaskIndex");
        setProperties(event, taskIndex);
        taskIndexRepository.save(taskIndex);
        log.info("Task index updated: {}", taskIndex);
    }

    public void deleteTaskIndex(Event event) {
        Long id = ((Number) event.payload().get("id")).longValue();
        TaskIndex taskIndex = findOrThrow(taskIndexRepository, id, "TaskIndex");
        taskIndexRepository.delete(taskIndex);
        log.info("Task index deleted: {}", taskIndex);
    }

    private void setProperties(Event event, TaskIndex taskIndex) {
        taskIndex.setId(((Number) event.payload().get("id")).longValue());
        taskIndex.setTitle((String) event.payload().get("title"));
        taskIndex.setDescription((String) event.payload().get("description"));
        taskIndex.setStatus(TaskStatus.valueOf((String) event.payload().get("status")));
        taskIndex.setPriority(TaskPriority.valueOf((String) event.payload().get("priority")));
        taskIndex.setDeadline(LocalDateTime.parse((String) event.payload().get("deadline")));
        taskIndex.setAssignedUserId(String.valueOf(event.payload().get("assignedUserId")));
        taskIndex.setProjectId(String.valueOf(event.payload().get("projectId")));
    }

}