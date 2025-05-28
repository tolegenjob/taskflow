package com.example.TaskFlow.Repository;

import com.example.TaskFlow.Entity.EventLog;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLogRepository extends MongoRepository<EventLog, ObjectId> {

    @NonNull
    Page<EventLog> findAll(@NonNull Pageable pageable);

}
