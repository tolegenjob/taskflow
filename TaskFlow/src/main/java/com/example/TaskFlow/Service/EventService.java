package com.example.TaskFlow.Service;

import com.example.TaskFlow.Config.KafkaProperties;
import com.example.TaskFlow.DTO.Event.Event;
import com.example.TaskFlow.Enum.EventType;
import com.example.TaskFlow.Exception.BadEventException;
import com.example.TaskFlow.Message.Producer.DlqEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.TaskFlow.Util.MessageUtil.sendDlqToKafka;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final EventLogService eventLogService;
    private final DlqEventProducer dlqEventProducer;
    private final KafkaProperties kafkaProperties;
    private final TaskIndexService taskIndexService;
    private final CommentIndexService commentIndexService;
    private final ProjectIndexService projectIndexService;

    public void handleEvent(Event event) {
        switch (event.eventType()) {
            case EventType.TASK_CREATED -> taskIndexService.createTaskIndex(event);
            case EventType.TASK_UPDATED -> taskIndexService.updateTaskIndex(event);
            case EventType.TASK_DELETED -> taskIndexService.deleteTaskIndex(event);
            case EventType.COMMENT_CREATED -> commentIndexService.createCommentIndex(event);
            case EventType.COMMENT_UPDATED -> commentIndexService.updateCommentIndex(event);
            case EventType.COMMENT_DELETED -> commentIndexService.deleteCommentIndex(event);
            case EventType.PROJECT_CREATED -> projectIndexService.createProjectIndex(event);
            case EventType.PROJECT_UPDATED, PROJECT_ARCHIVED -> projectIndexService.updateProjectIndex(event);
            case EventType.PROJECT_DELETED -> projectIndexService.deleteProjectIndex(event);
            default -> {
                sendDlqToKafka(dlqEventProducer,
                        kafkaProperties.topic().dlq(),
                        event,
                        "Invalid EventType");
                throw new BadEventException("Invalid EventType");
            }
        }
        log.info("Handled event: {}", event);
        eventLogService.createEventLog(event);
    }
}
