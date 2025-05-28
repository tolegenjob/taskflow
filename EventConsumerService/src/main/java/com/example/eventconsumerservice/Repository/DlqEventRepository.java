package com.example.eventconsumerservice.Repository;

import com.example.eventconsumerservice.Entity.DlqEvent;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface DlqEventRepository extends MongoRepository<DlqEvent, ObjectId> {

    @NonNull
    Page<DlqEvent> findAll(@NonNull Pageable pageable);

}
