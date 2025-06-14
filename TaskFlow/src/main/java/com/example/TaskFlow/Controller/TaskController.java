package com.example.TaskFlow.Controller;

import com.example.TaskFlow.DTO.Request.TaskCreateRequest;
import com.example.TaskFlow.DTO.Request.TaskUpdateRequest;
import com.example.TaskFlow.DTO.Response.TaskCreateResponse;
import com.example.TaskFlow.DTO.Response.TaskResponse;
import com.example.TaskFlow.DTO.Response.TaskUpdateResponse;
import com.example.TaskFlow.Security.AccessService;
import com.example.TaskFlow.Service.TaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final AccessService accessService;

    @PostMapping
    public ResponseEntity<TaskCreateResponse> createTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid TaskCreateRequest taskCreateRequest
    ) {
        if (!accessService.canCreateTask(userDetails, taskCreateRequest)) {
            return ResponseEntity.status(403).build();
        }
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
    public ResponseEntity<TaskResponse> getTaskById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ) {
        if (!accessService.canGetTask(userDetails, id)) {
            return ResponseEntity.status(403).build();
        }
        TaskResponse taskResponse = TaskResponse
                .toDTO(taskService.getTaskById(id));
        return ResponseEntity.ok(taskResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskUpdateResponse> updateTaskById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestBody @Valid TaskUpdateRequest taskUpdateRequest
    ) {
        if (!accessService.canUpdateDeleteTask(userDetails, id)) {
            return ResponseEntity.status(403).build();
        }
        TaskUpdateResponse taskUpdateResponse = TaskUpdateResponse
                .toDTO(taskService.updateTaskById(id, taskUpdateRequest));
        return ResponseEntity.ok(taskUpdateResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskResponse> deleteTaskById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ) {
        if (!accessService.canUpdateDeleteTask(userDetails, id)) {
            return ResponseEntity.status(403).build();
        }
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
