package com.example.TaskFlow.Entity;

import com.example.TaskFlow.Enum.LogLevel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document("log_entries")
@Getter
@Setter
public class LogEntry {

    @Id
    private ObjectId id;
    private LogLevel level;
    private String message;
    @CreatedDate
    private LocalDateTime timestamp;
    private Map<String, Object> context;

}
