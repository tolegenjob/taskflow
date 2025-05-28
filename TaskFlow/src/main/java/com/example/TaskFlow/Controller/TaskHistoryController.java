package com.example.TaskFlow.Controller;

import com.example.TaskFlow.DTO.Response.TaskHistoryResponse;
import com.example.TaskFlow.Service.TaskHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks/{taskId}/history")
public class TaskHistoryController {

    private final TaskHistoryService taskHistoryService;

    @GetMapping
    public ResponseEntity<List<TaskHistoryResponse>> getHistory(@PathVariable Long taskId) {
        List<TaskHistoryResponse> history = taskHistoryService
                .getAllTaskHistories(taskId)
                .stream()
                .map(TaskHistoryResponse::toDTO)
                .toList();
        return ResponseEntity.ok(history);
    }

}
