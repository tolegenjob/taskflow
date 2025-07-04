package com.example.TaskFlow.Controller;

import com.example.TaskFlow.DTO.Request.ProjectCreateRequest;
import com.example.TaskFlow.DTO.Request.ProjectUpdateRequest;
import com.example.TaskFlow.DTO.Response.ProjectCreateResponse;
import com.example.TaskFlow.DTO.Response.ProjectResponse;
import com.example.TaskFlow.DTO.Response.ProjectUpdateResponse;
import com.example.TaskFlow.Security.AccessService;
import com.example.TaskFlow.Service.ProjectService;
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
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final AccessService accessService;

    @PostMapping
    public ResponseEntity<ProjectCreateResponse> createProject(
            @RequestBody @Valid ProjectCreateRequest projectCreateRequest
    ) {
        ProjectCreateResponse projectCreateResponse = ProjectCreateResponse.toDTO(projectService.createProject(projectCreateRequest));
        return ResponseEntity.ok(projectCreateResponse);
    }

    @GetMapping
    public ResponseEntity<Page<ProjectResponse>> getAllProjects(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ProjectResponse> projectResponses = projectService.getAllProjects(pageable)
                .map(ProjectResponse::toDTO);
        return ResponseEntity.ok(projectResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ) {
        if (!accessService.canGetProject(userDetails, id)) {
            return ResponseEntity.status(403).build();
        }
        ProjectResponse projectResponse = ProjectResponse.toDTO(projectService.getProjectById(id));
        return ResponseEntity.ok(projectResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectUpdateResponse> updateProjectById(
            @PathVariable Long id,
            @RequestBody @Valid ProjectUpdateRequest projectUpdateRequest
    ) {
        ProjectUpdateResponse projectUpdateResponse = ProjectUpdateResponse.toDTO(projectService.updateProjectById(id, projectUpdateRequest));
        return ResponseEntity.ok(projectUpdateResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProjectResponse> deleteProjectById(@PathVariable Long id) {
        projectService.deleteProjectById(id);
        return ResponseEntity.noContent().build();
    }

}
