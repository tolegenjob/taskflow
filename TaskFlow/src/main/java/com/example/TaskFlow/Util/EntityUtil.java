package com.example.TaskFlow.Util;

import com.example.TaskFlow.Config.FileStorageProperties;
import com.example.TaskFlow.Exception.DoesNotBelongToEntityException;
import com.example.TaskFlow.Exception.EntityNotFoundException;
import com.example.TaskFlow.Exception.FileNotValidException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.multipart.MultipartFile;

public class EntityUtil {

    public static <T, ID> T findOrThrow(CrudRepository<T, ID> repository, ID id, String entityName) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("%s with id %s not found".formatted(entityName, id)));
    }

    public static void validateBelongsTo(
            Long expectedParentId,
            Long actualParentId,
            String childEntityName,
            Long childId,
            String parentEntityName
    ) {
        if (!expectedParentId.equals(actualParentId)) {
            throw new DoesNotBelongToEntityException(
                    "%s with id %d doesn't belong to the %s with id %d"
                            .formatted(childEntityName, childId, parentEntityName, expectedParentId)
            );
        }
    }

    public static void validateFile(MultipartFile file, FileStorageProperties fileStorageProperties) {
        if (file.getSize() > fileStorageProperties.maxSize()) {
            throw new FileNotValidException("File too large. Max size is 10 MB.");
        }

        if (!fileStorageProperties.allowedTypes().contains(file.getContentType())) {
            throw new FileNotValidException("Unsupported file type: %s".formatted(file.getContentType()));
        }
    }

}
