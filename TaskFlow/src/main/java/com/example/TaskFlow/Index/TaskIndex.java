package com.example.TaskFlow.Index;

import com.example.TaskFlow.Enum.TaskPriority;
import com.example.TaskFlow.Enum.TaskStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(indexName = "tasks")
public class TaskIndex {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;

    @Field(type = FieldType.Keyword)
    private TaskStatus status;

    @Field(type = FieldType.Keyword)
    private TaskPriority priority;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime deadline;

    @Field(type = FieldType.Keyword)
    private String assignedUserId;

    @Field(type = FieldType.Keyword)
    private String projectId;

}