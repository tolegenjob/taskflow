package com.example.TaskFlow.Controller;

import com.example.TaskFlow.DTO.Response.DocumentFileResponse;
import com.example.TaskFlow.Service.DocumentFileService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class DocumentFileController {

    private final DocumentFileService documentFileService;

    @PostMapping("/tasks/{taskId}/documents")
    public ResponseEntity<DocumentFileResponse> uploadDocument(
            @PathVariable Long taskId,
            @RequestParam("file") @NotNull MultipartFile file
    ) {
        DocumentFileResponse documentFileResponse = DocumentFileResponse
                .toDTO(documentFileService.uploadDocument(taskId, file));
        return ResponseEntity.ok(documentFileResponse);
    }

    @GetMapping("/tasks/{taskId}/documents")
    public ResponseEntity<List<DocumentFileResponse>> listDocuments(
            @PathVariable Long taskId
    ) {
        List<DocumentFileResponse> documentFileResponses = documentFileService
                .getDocumentFilesByTaskId(taskId)
                .stream()
                .map(DocumentFileResponse::toDTO)
                .toList();
        return ResponseEntity.ok(documentFileResponses);
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable String id) {
        DocumentFileService.DocumentResource downloadedDocumentFile = documentFileService
                .downloadDocumentFile(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(downloadedDocumentFile.metadata().getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"%s\"".formatted(downloadedDocumentFile.metadata().getFileName()))
                .body(downloadedDocumentFile.resource());
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String id) {
        documentFileService.deleteDocumentFile(id);
        return ResponseEntity.noContent().build();
    }
}
