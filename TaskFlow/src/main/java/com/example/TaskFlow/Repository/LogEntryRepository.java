package com.example.TaskFlow.Repository;

import com.example.TaskFlow.Entity.LogEntry;
import com.example.TaskFlow.Enum.LogLevel;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface LogEntryRepository extends MongoRepository<LogEntry, ObjectId> {

    @NonNull
    Page<LogEntry> findAll(@NonNull Pageable pageable);

    @NonNull
    Page<LogEntry> findByLevel(@NonNull Pageable pageable, LogLevel level);

}
