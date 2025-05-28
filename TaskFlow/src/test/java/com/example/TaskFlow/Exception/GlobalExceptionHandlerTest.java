package com.example.TaskFlow.Exception;

import com.example.TaskFlow.Config.KafkaProperties;
import com.example.TaskFlow.Message.Producer.LogEntryEventProducer;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @Mock
    private KafkaProperties kafkaProperties;
    @Mock
    private KafkaProperties.Topic topic;
    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Mock
    private LogEntryEventProducer logEntryEventProducer;
    @Mock
    private ServletWebRequest request;
    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;


    @BeforeEach
    void setUp() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setMethod("POST");
        mockRequest.setRequestURI("/test-endpoint");
        when(kafkaProperties.topic()).thenReturn(topic);
        when(kafkaProperties.topic().logs()).thenReturn("test-topic");
        when(request.getRequest()).thenReturn(httpServletRequest);
        when(request.getRequest().getMethod()).thenReturn("POST");
        when(request.getDescription(false)).thenReturn("/test-endpoint");
    }

    @Test
    void handleEntityNotFoundException_shouldReturnNotFoundResponse() {
        String errorMessage = "Task not found";
        EntityNotFoundException exception = new EntityNotFoundException(errorMessage);

        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleEntityNotFoundException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody().message());
        assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), response.getBody().error());
        assertNotNull(response.getBody().timestamp());
    }
    @Test
    void handleDoesNotBelongToEntityException_shouldReturnConflictResponse() {
        String errorMessage = "Task doesn't belong to user";
        DoesNotBelongToEntityException exception =
                new DoesNotBelongToEntityException(errorMessage);

        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleDoesNotBelongToEntityException(exception, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(errorMessage, response.getBody().message());
        assertEquals(HttpStatus.CONFLICT.getReasonPhrase(), response.getBody().error());
    }

    @Test
    void handleMethodArgumentNotValidException_shouldReturnBadRequest() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getMessage()).thenReturn("Validation failed");
        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleBadRequestException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), response.getBody().error());
    }

    @Test
    void handleHttpMessageNotReadableException_shouldReturnBadRequest() {
        HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);
        when(exception.getMessage()).thenReturn("Malformed JSON request");
        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleBadRequestException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void handleFileNotValidException_shouldReturnBadRequest() {
        FileNotValidException exception = new FileNotValidException("Invalid file type");

        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleBadRequestException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid file type", response.getBody().message());
    }

    @Test
    void handleMaxUploadSizeExceededException_shouldReturnPayloadTooLarge() {
        MaxUploadSizeExceededException exception =
                new MaxUploadSizeExceededException(10485760);

        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleMaxUploadSizeExceededException(exception, request);

        assertEquals(413, response.getStatusCode().value());
        assertEquals("Payload Too Large", response.getBody().error());
    }

    @Test
    void handleFileStorageException_shouldReturnInternalServerError() {
        FileStorageException exception =
                new FileStorageException("File storage error", new IOException());

        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleAll(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("File storage error", response.getBody().message());
    }

    @Test
    void handleGenericException_shouldReturnInternalServerError() {
        Exception exception = new Exception("Unexpected error");

        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleAll(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error", response.getBody().message());
    }
}
