package com.example.TaskFlow.Repository;

import com.example.TaskFlow.Entity.TaskHistory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskHistoryRepository extends MongoRepository<TaskHistory, ObjectId> {

    List<TaskHistory> findAllByTaskId(Long taskId);

}
