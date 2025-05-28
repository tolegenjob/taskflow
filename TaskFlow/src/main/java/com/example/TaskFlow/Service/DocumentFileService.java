package com.example.TaskFlow.Service;

import com.example.TaskFlow.Config.FileStorageProperties;
import com.example.TaskFlow.Entity.DocumentFile;
import com.example.TaskFlow.Exception.EntityNotFoundException;
import com.example.TaskFlow.Exception.FileStorageException;
import com.example.TaskFlow.Repository.DocumentFileRepository;
import com.example.TaskFlow.Repository.TaskRepository;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.example.TaskFlow.Util.EntityUtil.findOrThrow;
import static com.example.TaskFlow.Util.EntityUtil.validateFile;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentFileService {

    private final TaskRepository taskRepository;
    private final FileStorageProperties fileStorageProperties;
    private final DocumentFileRepository documentFileRepository;
    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations gridFsOperations;

    public record DocumentResource(DocumentFile metadata, GridFsResource resource) { }

    public DocumentFile uploadDocument(Long taskId, MultipartFile file) {

        findOrThrow(taskRepository, taskId, "Task");

        validateFile(file, fileStorageProperties);

        ObjectId gridFsId;
        try {
            gridFsId = gridFsTemplate.store(
                    file.getInputStream(),
                    file.getOriginalFilename(),
                    file.getContentType()
            );
        } catch (IOException e) {
            throw new FileStorageException("Error while saving DocumentFile in GridFS", e);
        }

        log.debug("DocumentFile was saved in GridFS with id {}", gridFsId);

        DocumentFile doc = new DocumentFile();
        doc.setTaskId(taskId);
        doc.setFileName(file.getOriginalFilename());
        doc.setFileType(file.getContentType());
        doc.setGridFsId(gridFsId);
        doc.setSize(file.getSize());

        DocumentFile saved = documentFileRepository.save(doc);
        log.info("Metadata of documentFile was saved with id {}", saved.getId());
        return saved;

    }

    public List<DocumentFile> getDocumentFilesByTaskId(Long taskId) {

        findOrThrow(taskRepository, taskId, "Task");
        log.debug("Documents of task {} have gotten.", taskId);
        return documentFileRepository.findByTaskId(taskId);

    }

    public DocumentResource downloadDocumentFile(String id) {

        ObjectId docId = new ObjectId(id);
        DocumentFile meta = findOrThrow(documentFileRepository, docId, "DocumentFile");
        GridFSFile gridFSFile = gridFsTemplate.findOne(query(where("_id").is(meta.getGridFsId())));
        if (gridFSFile == null) {
            throw new EntityNotFoundException("GridFsFile with id %s not found.".formatted(meta.getGridFsId()));
        }
        GridFsResource gridFsResource = gridFsOperations.getResource(gridFSFile);
        log.info("Document with id {} was downloaded.", id);
        return new DocumentResource(meta, gridFsResource);
    }

    public void deleteDocumentFile(String id) {

        ObjectId docId = new ObjectId(id);
        DocumentFile meta = findOrThrow(documentFileRepository, docId, "DocumentFile");
        gridFsTemplate.delete(query(where("_id").is(meta.getGridFsId())));
        documentFileRepository.deleteById(docId);
        log.info("Document with id {} was deleted.", id);

    }

}
