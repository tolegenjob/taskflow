package com.example.TaskFlow.Service;

import com.example.TaskFlow.Config.KafkaProperties;
import com.example.TaskFlow.DTO.Request.CommentCreateRequest;
import com.example.TaskFlow.DTO.Request.CommentUpdateRequest;
import com.example.TaskFlow.Entity.Comment;
import com.example.TaskFlow.Entity.Task;
import com.example.TaskFlow.Enum.EntityType;
import com.example.TaskFlow.Enum.EventType;
import com.example.TaskFlow.Enum.LogLevel;
import com.example.TaskFlow.Message.Producer.EventLogProducer;
import com.example.TaskFlow.Message.Producer.LogEntryEventProducer;
import com.example.TaskFlow.Repository.CommentRepository;
import com.example.TaskFlow.Repository.TaskRepository;
import com.example.TaskFlow.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.example.TaskFlow.Util.EntityUtil.findOrThrow;
import static com.example.TaskFlow.Util.EntityUtil.validateBelongsTo;
import static com.example.TaskFlow.Util.MessageUtil.sendEventLogToKafka;
import static com.example.TaskFlow.Util.MessageUtil.sendLogEntryEventToKafka;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final LogEntryEventProducer logEntryEventProducer;
    private final KafkaProperties kafkaProperties;
    private final EventLogProducer eventLogProducer;

    public Comment createComment(Long taskId, CommentCreateRequest commentCreateRequest) {
        Comment comment = new Comment();
        comment.setContent(commentCreateRequest.content());
        Task task = findOrThrow(taskRepository, taskId, "Task");
        comment.setTask(task);
        comment.setUser(findOrThrow(userRepository, commentCreateRequest.userId(), "User"));
        Comment saved = commentRepository.save(comment);
        log.info("Created comment with id: {}", saved.getId());
        Map<String, Object> context = getCommentDetails(saved, task.getAssignedUser().getId());
        sendLogEntryEventToKafka(
                logEntryEventProducer,
                kafkaProperties.topic().logs(),
                LogLevel.INFO,
                "Created comment with id: %s".formatted(saved.getId()),
                context);
        sendEventLogToKafka(eventLogProducer,
                kafkaProperties.topic().events(),
                EventType.COMMENT_CREATED,
                comment.getId(),
                EntityType.COMMENT,
                context);
        return saved;
    }

    public Page<Comment> getAllCommentsByTaskId(Long taskId, Pageable pageable) {

        findOrThrow(taskRepository, taskId, "Task");

        Page<Comment> comments = commentRepository.findByTaskId(taskId, pageable);
        log.info("Got all comments by task id: {}", taskId);
        return comments;
    }

    public Comment getCommentById(Long taskId, Long id) {

        findOrThrow(taskRepository, taskId, "Task");
        Comment comment = findOrThrow(commentRepository, id, "Comment");
        validateBelongsTo(taskId, comment.getTask().getId(), "Comment", id, "Task");

        log.info("Got comment with id: {} ", id);
        return comment;
    }

    public Comment updateCommentById(Long taskId, Long id, CommentUpdateRequest commentUpdateRequest) {

        findOrThrow(taskRepository, taskId, "Task");
        Comment comment = findOrThrow(commentRepository, id, "Comment");
        validateBelongsTo(taskId, comment.getTask().getId(), "Comment", id, "Task");
        Long authorId = findOrThrow(taskRepository, taskId, "Task").getAssignedUser().getId();
        comment.setContent(commentUpdateRequest.content());
        Comment saved = commentRepository.save(comment);
        log.info("Updated comment with id: {} ", id);
        Map<String, Object> context = getCommentDetails(saved, authorId);
        sendLogEntryEventToKafka(
                logEntryEventProducer,
                kafkaProperties.topic().logs(),
                LogLevel.INFO,
                "Updated comment with id: %s".formatted(saved.getId()),
                context);
        sendEventLogToKafka(eventLogProducer,
                kafkaProperties.topic().events(),
                EventType.COMMENT_UPDATED,
                comment.getId(),
                EntityType.COMMENT,
                context);
        return saved;
    }

    public void deleteCommentById(Long taskId, Long id) {

        findOrThrow(taskRepository, taskId, "Task");
        Comment comment = findOrThrow(commentRepository, id, "Comment");
        validateBelongsTo(taskId, comment.getTask().getId(), "Comment", id, "Task");

        commentRepository.delete(comment);
        log.info("Deleted comment with id: {} ", id);
        Long authorId = findOrThrow(taskRepository, taskId, "Task").getAssignedUser().getId();
        Map<String, Object> context = getCommentDetails(comment, authorId);
        sendLogEntryEventToKafka(
                logEntryEventProducer,
                kafkaProperties.topic().logs(),
                LogLevel.INFO,
                "Deleted comment with id: %s".formatted(comment.getId()),
                context);
        sendEventLogToKafka(eventLogProducer,
                kafkaProperties.topic().events(),
                EventType.COMMENT_DELETED,
                comment.getId(),
                EntityType.COMMENT,
                context);
    }

    private Map<String, Object> getCommentDetails(Comment comment, Long authorId) {
        return Map.of(
                "id", comment.getId(),
                "content", comment.getContent(),
                "taskId", comment.getTask().getId(),
                "taskAuthorId", authorId,
                "userId", comment.getUser().getId()
        );
    }

}
