package com.example.TaskFlow.Service;

import com.example.TaskFlow.DTO.Request.ProjectCreateRequest;
import com.example.TaskFlow.DTO.Request.ProjectUpdateRequest;
import com.example.TaskFlow.Entity.Project;
import com.example.TaskFlow.Entity.User;
import com.example.TaskFlow.Enum.ProjectStatus;
import com.example.TaskFlow.Repository.ProjectRepository;
import com.example.TaskFlow.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private User owner;
    private Project expectedProject;
    private ProjectCreateRequest projectCreateRequest;
    private ProjectUpdateRequest projectUpdateRequest;

    @BeforeEach
    void setUp() {
        owner = new User();
        ReflectionTestUtils.setField(owner, "id", 1L);
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setEmail("john.doe@example.com");
        expectedProject = new Project();
        ReflectionTestUtils.setField(expectedProject, "id", 1L);
        expectedProject.setName("ProjectName");
        expectedProject.setDescription("ProjectDescription");
        expectedProject.setStatus(ProjectStatus.ACTIVE);
        expectedProject.setOwner(owner);
        projectCreateRequest = new ProjectCreateRequest(
                "ProjectName", "ProjectDescription", ProjectStatus.ACTIVE, 1L
        );
        projectUpdateRequest = new ProjectUpdateRequest(
                "ProjectNewName", "ProjectNewDescription", ProjectStatus.COMPLETED
        );
    }

    @Test
    void createProject_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(projectRepository.save(any(Project.class))).thenReturn(expectedProject);
        Project result = projectService.createProject(projectCreateRequest);
        assertAll("Create Project",
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getName()).isEqualTo("ProjectName"),
                () -> assertThat(result.getDescription()).isEqualTo("ProjectDescription"),
                () -> assertThat(result.getStatus()).isEqualTo(ProjectStatus.ACTIVE)
        );
    }

    @Test
    void updateProject_completed_success() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(expectedProject));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Project result = projectService.updateProjectById(1L, projectUpdateRequest);
        assertAll("Update Project Completed",
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getName()).isEqualTo("ProjectNewName"),
                () -> assertThat(result.getDescription()).isEqualTo("ProjectNewDescription"),
                () -> assertThat(result.getStatus()).isEqualTo(ProjectStatus.COMPLETED)
        );
    }
}
