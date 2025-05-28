package com.example.eventconsumerservice.Exception;

import com.example.eventconsumerservice.Config.KafkaProperties;
import com.example.eventconsumerservice.Config.ServerProperties;
import com.example.eventconsumerservice.Enum.LogLevel;
import com.example.eventconsumerservice.Message.Producer.LogEntryEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static com.example.eventconsumerservice.Util.MessageUtil.sendLogEntryEventToKafka;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final LogEntryEventProducer logEntryEventProducer;
    private final KafkaProperties kafkaProperties;
    private final ServerProperties serverProperties;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(Throwable e,
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

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(Throwable e,
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Throwable e,
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

    private Map<String, Object> getErrorContext(Throwable ex,
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
