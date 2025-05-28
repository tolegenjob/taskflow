package com.example.TaskFlow.Service;

import com.example.TaskFlow.DTO.Request.TaskCreateRequest;
import com.example.TaskFlow.DTO.Request.TaskUpdateRequest;
import com.example.TaskFlow.Entity.Project;
import com.example.TaskFlow.Entity.Task;
import com.example.TaskFlow.Entity.User;
import com.example.TaskFlow.Enum.TaskPriority;
import com.example.TaskFlow.Enum.TaskStatus;
import com.example.TaskFlow.Exception.EntityNotFoundException;
import com.example.TaskFlow.Repository.ProjectRepository;
import com.example.TaskFlow.Repository.TaskRepository;
import com.example.TaskFlow.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskService taskService;

    private User mockUser;
    private Project mockProject;
    private Task expectedTask;
    private TaskCreateRequest taskCreateRequest;
    private TaskUpdateRequest taskUpdateRequest;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        ReflectionTestUtils.setField(mockUser, "id", 1L);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setEmail("john.doe@example.com");
        mockProject = new Project();
        ReflectionTestUtils.setField(mockProject, "id", 1L);
        mockProject.setName("ProjectName");
        expectedTask = new Task();
        ReflectionTestUtils.setField(expectedTask, "id", 1L);
        expectedTask.setTitle("TaskTitle");
        expectedTask.setDescription("TaskDescription");
        expectedTask.setStatus(TaskStatus.TODO);
        expectedTask.setPriority(TaskPriority.LOW);
        expectedTask.setDeadline(LocalDateTime.now().plusDays(2));
        expectedTask.setAssignedUser(mockUser);
        expectedTask.setProject(mockProject);
        taskCreateRequest = new TaskCreateRequest(
                "TaskTitle", "TaskDescription", TaskStatus.TODO, TaskPriority.LOW,
                LocalDateTime.now().plusDays(2), 1L, 1L
        );
        taskUpdateRequest = new TaskUpdateRequest(
                "TaskNewTitle", "TaskNewDescription", TaskStatus.IN_PROGRESS,TaskPriority.MEDIUM,
                LocalDateTime.now().plusDays(2),1L
        );
    }

    @Test
    void createTask_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(mockProject));
        when(taskRepository.save(any(Task.class))).thenReturn(expectedTask);
        Task result = taskService.createTask(taskCreateRequest);
        assertAll("Create Task",
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getTitle()).isEqualTo("TaskTitle"),
                () -> assertThat(result.getDescription()).isEqualTo("TaskDescription")
        );
    }

    @Test
    void updateTask_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(expectedTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Task result = taskService.updateTaskById(1L, taskUpdateRequest);
        assertAll("Update Task",
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getTitle()).isEqualTo("TaskNewTitle"),
                () -> assertThat(result.getDescription()).isEqualTo("TaskNewDescription"),
                () -> assertThat(result.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS),
                () -> assertThat(result.getPriority()).isEqualTo(TaskPriority.MEDIUM)
        );
    }

    @Test
    void getTaskById_success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(expectedTask));
        Task result = taskService.getTaskById(1L);
        assertAll("Get Task",
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getTitle()).isEqualTo("TaskTitle"),
                () -> assertThat(result.getDescription()).isEqualTo("TaskDescription")
        );
    }

    @Test
    void deleteTaskById_success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(expectedTask));
        doNothing().when(taskRepository).deleteById(1L);
        taskService.deleteTaskById(1L);
        verify(taskRepository).findById(1L);
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void createTask_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> taskService.createTask(taskCreateRequest));
    }

    @Test
    void createTask_projectNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> taskService.createTask(taskCreateRequest));
    }

    @Test
    void updateTaskById_TaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> taskService.updateTaskById(1L, taskUpdateRequest));
    }

    @Test
    void getTaskById_TaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> taskService.getTaskById(1L));
    }

    @Test
    void deleteTaskById_TaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> taskService.deleteTaskById(1L));
    }

    @Test
    void filterTasksByStatusAndPriority_success() {

        Task task1 = new Task();
        ReflectionTestUtils.setField(task1, "id", 2L);
        task1.setTitle("TaskTitle2");
        task1.setDescription("TaskDescription2");
        task1.setStatus(TaskStatus.IN_PROGRESS);
        task1.setPriority(TaskPriority.HIGH);
        task1.setDeadline(LocalDateTime.now().plusDays(2));

        Task task2 = new Task();
        ReflectionTestUtils.setField(task2, "id", 3L);
        task2.setTitle("TaskTitle3");
        task2.setDescription("TaskDescription3");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setPriority(TaskPriority.HIGH);
        task2.setDeadline(LocalDateTime.now().plusDays(2));

        Page<Task> filteredMockPage = new PageImpl<>(List.of(task1, task2));

        when(taskRepository.findByStatusAndPriority(TaskStatus.IN_PROGRESS, TaskPriority.HIGH, pageable)).thenReturn(filteredMockPage);

        Page<Task> result = taskService
                .filterTasksByStatusAndPriority("IN_PROGRESS", "HIGH", pageable);

        assertThat(result.getContent())
                .hasSize(2)
                .extracting(Task::getStatus, Task::getPriority)
                .containsExactly(
                        tuple(TaskStatus.IN_PROGRESS, TaskPriority.HIGH),
                        tuple(TaskStatus.IN_PROGRESS, TaskPriority.HIGH)
                );
    }

}
