package com.example.TaskFlow.DTO.Response;

import com.example.TaskFlow.Entity.DocumentFile;

import java.time.LocalDateTime;

public record DocumentFileResponse (
        String id,
        Long taskId,
        String fileName,
        String fileType,
        LocalDateTime uploadedAt,
        Long size
) { public static DocumentFileResponse toDTO (DocumentFile documentFile) {
        return new DocumentFileResponse(
            documentFile.getId().toString(),
            documentFile.getTaskId(),
            documentFile.getFileName(),
            documentFile.getFileType(),
            documentFile.getUploadedAt(),
            documentFile.getSize());
    }
}
