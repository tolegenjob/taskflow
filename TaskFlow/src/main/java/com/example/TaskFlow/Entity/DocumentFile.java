package com.example.TaskFlow.Entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("document_files")
@Getter
@Setter
public class DocumentFile {

    @Id
    private ObjectId id;
    private Long taskId;
    private String fileName;
    private String fileType;
    private ObjectId gridFsId;
    @CreatedDate
    private LocalDateTime uploadedAt;
    private Long size;

}
