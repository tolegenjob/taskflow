package com.example.eventconsumerservice.Entity;

import com.example.eventconsumerservice.Enum.EntityType;
import com.example.eventconsumerservice.Enum.EventType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document("processed_events")
@Getter
@Setter
public class ProcessedEvent {

    @Id
    private ObjectId id;
    private EventType eventType;
    private Long entityId;
    private EntityType entityType;
    private Map<String, Object> payload;
    @CreatedDate
    private LocalDateTime createdAt;

}
