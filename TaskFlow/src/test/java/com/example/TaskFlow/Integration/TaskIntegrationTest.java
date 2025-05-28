package com.example.TaskFlow.Integration;

import com.example.TaskFlow.DTO.Request.ProjectCreateRequest;
import com.example.TaskFlow.DTO.Request.TaskCreateRequest;
import com.example.TaskFlow.DTO.Request.TaskUpdateRequest;
import com.example.TaskFlow.DTO.Request.UserCreateRequest;
import com.example.TaskFlow.Enum.ProjectStatus;
import com.example.TaskFlow.Enum.TaskPriority;
import com.example.TaskFlow.Enum.TaskStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Long userId;
    private Long projectId;

    @BeforeEach
    void setUp() throws Exception {

        UserCreateRequest userCreateRequest = new UserCreateRequest(
                "John", "Doe", "john.doe@example.com"
        );
        String userJson = objectMapper.writeValueAsString(userCreateRequest);
        String userCreateResponse = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").isNumber(),
                        jsonPath("$.firstName").value("John")
                )
                .andReturn().getResponse().getContentAsString();
        userId = objectMapper.readTree(userCreateResponse).get("id").asLong();

        ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest(
                "ProjectName","ProjectDescription", ProjectStatus.ACTIVE, userId
        );
        String projectJson = objectMapper.writeValueAsString(projectCreateRequest);
        String projectCreateResponse = mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").isNumber(),
                        jsonPath("$.name").value("ProjectName")
                )
                .andReturn().getResponse().getContentAsString();
        projectId = objectMapper.readTree(projectCreateResponse).get("id").asLong();
    }

    @Test
    void crudTask_success() throws Exception {

        TaskCreateRequest taskCreateRequest = new TaskCreateRequest(
                "TaskTitle", "TaskDescription", TaskStatus.TODO, TaskPriority.MEDIUM,
                LocalDateTime.now().plusDays(2), userId, projectId
        );
        String createJson = objectMapper.writeValueAsString(taskCreateRequest);
        String TaskCreateResponse = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").isNumber(),
                        jsonPath("$.title").value("TaskTitle")
                )
                .andReturn()
                .getResponse()
                .getContentAsString();
        Long taskId = objectMapper.readTree(TaskCreateResponse).get("id").asLong();

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        mockMvc.perform(get("/tasks/" + taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId));

        TaskUpdateRequest updateRequest = new TaskUpdateRequest(
                "TaskNewTitle", "TaskNewDescription", TaskStatus.IN_PROGRESS,
                TaskPriority.HIGH, LocalDateTime.now().plusDays(5), userId
        );
        String updateJson = objectMapper.writeValueAsString(updateRequest);
        mockMvc.perform(put("/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("TaskNewTitle"));

        mockMvc.perform(delete("/tasks/" + taskId))
                .andExpect(status().isNoContent());

    }

    @Test
    void createTask_shouldReturnBadRequest_whenDescriptionIsNull() throws Exception {
        TaskCreateRequest invalidRequest = new TaskCreateRequest(
                "TaskTitle", null, TaskStatus.TODO, TaskPriority.MEDIUM,
                LocalDateTime.now().plusDays(2), userId, projectId
        );
        String invalidJson = objectMapper.writeValueAsString(invalidRequest);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("description")));
    }

    @Test
    void createTask_shouldReturnBadRequest_whenDeadlineIsPast() throws Exception {
        TaskCreateRequest invalidRequest = new TaskCreateRequest(
                "TaskTitle", "Description", TaskStatus.TODO, TaskPriority.MEDIUM,
                LocalDateTime.now().minusDays(30), userId, projectId
        );
        String invalidJson = objectMapper.writeValueAsString(invalidRequest);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("deadline")));
    }
}
