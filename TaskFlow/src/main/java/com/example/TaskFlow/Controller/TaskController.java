package com.example.TaskFlow.Controller;

import com.example.TaskFlow.DTO.Request.TaskCreateRequest;
import com.example.TaskFlow.DTO.Request.TaskUpdateRequest;
import com.example.TaskFlow.DTO.Response.TaskCreateResponse;
import com.example.TaskFlow.DTO.Response.TaskResponse;
import com.example.TaskFlow.DTO.Response.TaskUpdateResponse;
import com.example.TaskFlow.Service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskCreateResponse> createTask(
            @RequestBody @Valid TaskCreateRequest taskCreateRequest) {
        TaskCreateResponse taskCreateResponse = TaskCreateResponse
                .toDTO(taskService.createTask(taskCreateRequest));
        return ResponseEntity.ok(taskCreateResponse);
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getAllTasks(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<TaskResponse> taskResponses = taskService
                .getAllTasks(pageable)
                .map(TaskResponse::toDTO);
        return ResponseEntity.ok(taskResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        TaskResponse taskResponse = TaskResponse
                .toDTO(taskService.getTaskById(id));
        return ResponseEntity.ok(taskResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskUpdateResponse> updateTaskById(
            @PathVariable Long id, @RequestBody @Valid TaskUpdateRequest taskUpdateRequest
    ) {
        TaskUpdateResponse taskUpdateResponse = TaskUpdateResponse
                .toDTO(taskService.updateTaskById(id, taskUpdateRequest));
        return ResponseEntity.ok(taskUpdateResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskResponse> deleteTaskById(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<TaskResponse>> filterTasks(
            @RequestParam String status,
            @RequestParam String priority,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<TaskResponse> result = taskService
                .filterTasksByStatusAndPriority(status, priority, pageable)
                .map(TaskResponse::toDTO);
        return ResponseEntity.ok(result);
    }
}
