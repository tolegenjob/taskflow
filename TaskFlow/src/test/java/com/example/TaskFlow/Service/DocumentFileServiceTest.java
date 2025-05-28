package com.example.TaskFlow.Service;

import com.example.TaskFlow.Config.FileStorageProperties;
import com.example.TaskFlow.Entity.DocumentFile;
import com.example.TaskFlow.Entity.Task;
import com.example.TaskFlow.Repository.DocumentFileRepository;
import com.example.TaskFlow.Repository.TaskRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentFileServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private DocumentFileRepository documentFileRepository;
    @Mock
    private GridFsTemplate gridFsTemplate;
    @Mock
    private FileStorageProperties fileStorageProperties;

    @InjectMocks
    private DocumentFileService documentFileService;

    private MultipartFile multipartFile;
    private DocumentFile documentFile;
    private ObjectId gridFsId;
    private ObjectId documentFileId;

    @BeforeEach
    void setUp() {
        documentFileId = new ObjectId();
        multipartFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Hello World".getBytes()
        );
        gridFsId = new ObjectId();
        documentFile = new DocumentFile();
        ReflectionTestUtils.setField(documentFile, "id", documentFileId);
        documentFile.setTaskId(1L);
        documentFile.setFileName("test.txt");
        documentFile.setFileType("text/plain");
        documentFile.setGridFsId(gridFsId);
        documentFile.setSize(multipartFile.getSize());
    }

    @Test
    void uploadDocument_success() {
        when(fileStorageProperties.maxSize()).thenReturn(10 * 1024 * 1024L);
        when(fileStorageProperties.allowedTypes()).thenReturn(Set.of("text/plain"));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(mock(Task.class)));
        when(gridFsTemplate.store(any(InputStream.class), eq("test.txt"), eq("text/plain")))
                .thenReturn(gridFsId);
        when(documentFileRepository.save(any(DocumentFile.class))).thenReturn(documentFile);

        DocumentFile result = documentFileService.uploadDocument(1L, multipartFile);
        ArgumentCaptor<DocumentFile> captor = ArgumentCaptor.forClass(DocumentFile.class);

        assertThat(result).isEqualTo(documentFile);
        verify(documentFileRepository).save(captor.capture());
        assertThat(captor.getValue().getGridFsId()).isEqualTo(gridFsId);
    }

    @Test
    void getDocumentFilesByTaskId_success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(mock(Task.class)));
        when(documentFileRepository.findByTaskId(1L)).thenReturn(List.of(new DocumentFile()));

        List<DocumentFile> result = documentFileService.getDocumentFilesByTaskId(1L);

        assertEquals(1, result.size());
        verify(documentFileRepository).findByTaskId(1L);
    }

    @Test
    void deleteDocumentFile_success() {
        when(documentFileRepository.findById(documentFileId)).thenReturn(Optional.of(documentFile));

        documentFileService.deleteDocumentFile(documentFileId.toString());

        verify(gridFsTemplate).delete(any());
        verify(documentFileRepository).deleteById(documentFileId);
    }
}
