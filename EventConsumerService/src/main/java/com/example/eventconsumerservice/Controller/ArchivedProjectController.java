package com.example.eventconsumerservice.Controller;

import com.example.eventconsumerservice.DTO.Response.ArchivedProjectResponse;
import com.example.eventconsumerservice.Service.ArchivedProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/archived-projects")
public class ArchivedProjectController {

    private final ArchivedProjectService archivedProjectService;

    @GetMapping
    public ResponseEntity<Page<ArchivedProjectResponse>> getAllArchivedProjects(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ArchivedProjectResponse> archivedProjectResponses = archivedProjectService.getAllArchivedProjects(pageable)
                .map(ArchivedProjectResponse::toDTO);
        return ResponseEntity.ok(archivedProjectResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArchivedProjectResponse> getArchivedProjectById(@PathVariable Long id) {
        ArchivedProjectResponse archivedProjectResponse = ArchivedProjectResponse.toDTO(archivedProjectService.getArchivedProjectById(id));
        return ResponseEntity.ok(archivedProjectResponse);
    }

}
