package com.example.TaskFlow.Controller;

import com.example.TaskFlow.Migration.CommentsToElasticMigrationService;
import com.example.TaskFlow.Migration.ProjectsToElasticMigrationService;
import com.example.TaskFlow.Migration.TasksToElasticMigrationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/migration-elastic")
public class ElasticMigrationController {

    private final TasksToElasticMigrationService tasksToElasticMigrationService;
    private final CommentsToElasticMigrationService commentsToElasticMigrationService;
    private final ProjectsToElasticMigrationService projectsToElasticMigrationService;

    @GetMapping("/tasks")
    public ResponseEntity<String> migrateTasks() {
        tasksToElasticMigrationService.migrateAllTasksToElasticsearch();
        return ResponseEntity.ok("Migrated tasks");
    }

    @GetMapping("/comments")
    public ResponseEntity<String> migrateComments() {
        commentsToElasticMigrationService.migrateAllCommentsToElasticsearch();
        return ResponseEntity.ok("Migrated comments");
    }

    @GetMapping("/projects")
    public ResponseEntity<String> migrateProjects() {
        projectsToElasticMigrationService.migrateAllCommentsToElasticsearch();
        return ResponseEntity.ok("Migrated projects");
    }

}
