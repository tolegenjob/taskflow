package com.example.notificationservice.Util;

import com.example.notificationservice.Exception.EntityNotFoundException;
import org.springframework.data.repository.CrudRepository;

public class EntityUtil {

    public static <T, ID> T findOrThrow(CrudRepository<T, ID> repository, ID id, String entityName) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("%s with id %s not found".formatted(entityName, id)));
    }

}
