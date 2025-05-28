package com.example.TaskFlow.Message.Consumer;

import com.example.TaskFlow.DTO.Event.TaskHistoryEvent;
import com.example.TaskFlow.Service.TaskHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskHistoryEventConsumer {

    private final TaskHistoryService taskHistoryService;

    @RabbitListener(queues = "${spring.rabbitmq.history.queue}")
    public void receiveTaskHistoryEvent(TaskHistoryEvent event) {
        log.info("Received TaskHistoryEvent: {} {} {}",
                event.taskId(),
                event.action(),
                event.performedBy());
        taskHistoryService.createTaskHistory(
                event.taskId(),
                event.action(),
                event.performedBy(),
                event.details()
        );
    }

}
