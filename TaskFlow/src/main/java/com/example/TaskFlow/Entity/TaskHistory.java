package com.example.TaskFlow.Entity;

import com.example.TaskFlow.Enum.NotificationStatus;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document("task_histories")
@Getter
@Setter
public class TaskHistory {

    @Id
    private ObjectId id;
    private Long taskId;
    private NotificationStatus action;
    private Long performedBy;
    @CreatedDate
    private LocalDateTime timestamp;
    private Map<String, Object> details;
}
