package com.example.TaskFlow.Controller;

import com.example.TaskFlow.Service.CommentIndexService;
import com.example.TaskFlow.Service.ProjectIndexService;
import com.example.TaskFlow.Service.TaskIndexService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/analytics")
public class AnalyticsController {

    private final ProjectIndexService projectIndexService;
    private final TaskIndexService taskIndexService;
    private final CommentIndexService commentIndexService;

    @GetMapping("/tasks/status")
    public ResponseEntity<Map<String, Long>> analyzeTasksByStatus() {
        return ResponseEntity.ok(taskIndexService.getTaskCountByStatus());
    }

    @GetMapping("/tasks/priority")
    public ResponseEntity<Map<String, Long>> analyzeTasksByPriority() {
        return ResponseEntity.ok(taskIndexService.getTaskCountByPriority());
    }

    @GetMapping("/comments/user")
    public ResponseEntity<Map<String, Long>> analyzeCommentsByUser() {
        return ResponseEntity.ok(commentIndexService.getCommentCountByUser());
    }

    @GetMapping("/projects/status")
    public ResponseEntity<Map<String, Long>> analyzeProjectsByStatus() {
        return ResponseEntity.ok(projectIndexService.getProjectCountByStatus());
    }
}
