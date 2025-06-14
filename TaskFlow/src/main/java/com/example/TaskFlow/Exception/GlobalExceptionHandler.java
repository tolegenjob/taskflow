package com.example.TaskFlow.Exception;

import com.example.TaskFlow.Config.KafkaProperties;
import com.example.TaskFlow.Config.ServerProperties;
import com.example.TaskFlow.Enum.LogLevel;
import com.example.TaskFlow.Message.Producer.LogEntryEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static com.example.TaskFlow.Util.MessageUtil.sendLogEntryEventToKafka;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final LogEntryEventProducer logEntryEventProducer;
    private final KafkaProperties kafkaProperties;
    private final ServerProperties serverProperties;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e,
                                                                       ServletWebRequest request) {
        log.error("Entity not found: {}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(e.getMessage())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
        Map<String, Object> context = getErrorContext(e, request, errorResponse);
        sendLogEntryEventToKafka(
                logEntryEventProducer,
                kafkaProperties.topic().logs(),
                LogLevel.ERROR,
                "localhost:%s:Entity not found: %s".formatted(serverProperties.port(), e.getMessage()),
                context);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DoesNotBelongToEntityException.class)
    public ResponseEntity<ErrorResponse> handleDoesNotBelongToEntityException(DoesNotBelongToEntityException e,
                                                                              ServletWebRequest request) {
        log.error("Entity doesn't belong to requested one: {}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT)
                .message(e.getMessage())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
        Map<String, Object> context = getErrorContext(e, request, errorResponse);
        sendLogEntryEventToKafka(
                logEntryEventProducer,
                kafkaProperties.topic().logs(),
                LogLevel.ERROR,
                "localhost:%s:Entity doesn't belong to requested one: %s".formatted(serverProperties.port(), e.getMessage()),
                context);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            FileNotValidException.class,
            TokenExpiredException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception e,
                                                                   ServletWebRequest request) {

        String className = e.getClass().getSimpleName();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
        log.error("Bad request error: {} - {} ", className, e.getMessage());
        Map<String, Object> context = getErrorContext(e, request, errorResponse);
        sendLogEntryEventToKafka(
                logEntryEventProducer,
                kafkaProperties.topic().logs(),
                LogLevel.ERROR,
                "localhost:%s:Bad request error: %s - %s".formatted(serverProperties.port(), className, e.getMessage()),
                context);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e,
                                                                              ServletWebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.valueOf(413))
                .message(e.getMessage())
                .error(HttpStatus.valueOf(413).getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
        log.error("File size is too large: {}", e.getMessage());
        Map<String, Object> context = getErrorContext(e, request, errorResponse);
        sendLogEntryEventToKafka(
                logEntryEventProducer,
                kafkaProperties.topic().logs(),
                LogLevel.ERROR,
                "localhost:%s:File size is too large: %s".formatted(serverProperties.port(), e.getMessage()),
                context);
        return ResponseEntity.status(HttpStatus.valueOf(413)).body(errorResponse);
    }

    @ExceptionHandler({
            Exception.class,
            FileStorageException.class
    })
    public ResponseEntity<ErrorResponse> handleAll(Exception e,
                                                   ServletWebRequest request) {

        String className = e.getClass().getSimpleName();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
        log.error("Internal server error: {} - {} ", className, e.getMessage());
        Map<String, Object> context = getErrorContext(e, request, errorResponse);
        sendLogEntryEventToKafka(
                logEntryEventProducer,
                kafkaProperties.topic().logs(),
                LogLevel.ERROR,
                "localhost:%s:Internal server error: %s - %s".formatted(serverProperties.port(), className, e.getMessage()),
                context);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private Map<String, Object> getErrorContext(Exception ex,
                                                ServletWebRequest request,
                                                ErrorResponse errorResponse) {
        return Map.of(
                "path", request.getDescription(false),
                "httpMethod", request.getRequest().getMethod(),
                "exception", ex.getClass().getSimpleName(),
                "status", errorResponse.status(),
                "message", errorResponse.message(),
                "error", errorResponse.error(),
                "timestamp", errorResponse.timestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
        );
    }

}
