package com.example.TaskFlow.Entity;


import com.example.TaskFlow.Enum.EntityType;
import com.example.TaskFlow.Enum.EventType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document("event_logs")
@Getter
@Setter
public class EventLog {

    @Id
    private ObjectId id;
    private EventType eventType;
    private Long entityId;
    private EntityType entityType;
    private Map<String, Object> payload;
    @CreatedDate
    private LocalDateTime createdAt;
}
