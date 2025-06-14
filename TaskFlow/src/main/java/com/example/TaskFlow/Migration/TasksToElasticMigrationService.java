package com.example.TaskFlow.Migration;

import com.example.TaskFlow.Entity.Task;
import com.example.TaskFlow.Index.TaskIndex;
import com.example.TaskFlow.Repository.TaskIndexRepository;
import com.example.TaskFlow.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TasksToElasticMigrationService {

    private final TaskRepository taskRepository;
    private final TaskIndexRepository taskIndexRepository;

    @Transactional(readOnly = true)
    public void migrateAllTasksToElasticsearch() {
        List<Task> tasks = taskRepository.findAll();
        List<TaskIndex> taskIndicesToSave = tasks.stream()
                .filter(task -> !taskIndexRepository.existsById(task.getId()))
                .map(task -> {
                    TaskIndex taskIndex = new TaskIndex();
                    setProperties(task, taskIndex);
                    return taskIndex;
                })
                .toList();
        if (!taskIndicesToSave.isEmpty()) {
            taskIndexRepository.saveAll(taskIndicesToSave);
        }
    }

    private void setProperties(Task task, TaskIndex taskIndex) {
        taskIndex.setId(task.getId());
        taskIndex.setTitle(task.getTitle());
        taskIndex.setDescription((task.getDescription()));
        taskIndex.setStatus(task.getStatus());
        taskIndex.setPriority(task.getPriority());
        taskIndex.setDeadline(task.getDeadline());
        taskIndex.setAssignedUserId(String.valueOf(task.getAssignedUser().getId()));
        taskIndex.setProjectId(String.valueOf(task.getProject().getId()));
    }
}
