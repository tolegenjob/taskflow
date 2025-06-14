package com.example.TaskFlow.Controller;

import com.example.TaskFlow.Enum.TaskPriority;
import com.example.TaskFlow.Enum.TaskStatus;
import com.example.TaskFlow.Index.CommentIndex;
import com.example.TaskFlow.Index.ProjectIndex;
import com.example.TaskFlow.Index.TaskIndex;
import com.example.TaskFlow.Service.CommentIndexService;
import com.example.TaskFlow.Service.ProjectIndexService;
import com.example.TaskFlow.Service.TaskIndexService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/search")
public class SearchController {

    private final ProjectIndexService projectIndexService;
    private final TaskIndexService taskIndexService;
    private final CommentIndexService commentIndexService;

    @GetMapping("/tasks")
    public ResponseEntity<Page<TaskIndex>> searchTasks(
            @PageableDefault Pageable pageable,
            @RequestParam String query,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority) {
        Page<TaskIndex> response = taskIndexService.processSearch(query, status, priority, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/comments")
    public ResponseEntity<Page<CommentIndex>> searchComments(
            @PageableDefault Pageable pageable,
            @RequestParam String query,
            @RequestParam(required = false) Long taskId) {
        Page<CommentIndex> response = commentIndexService.processSearch(query, taskId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/projects")
    public ResponseEntity<Page<ProjectIndex>> searchProjects(
            @PageableDefault Pageable pageable,
            @RequestParam String query) {
        Page<ProjectIndex> response = projectIndexService.processSearch(query, pageable);
        return ResponseEntity.ok(response);
    }

}
