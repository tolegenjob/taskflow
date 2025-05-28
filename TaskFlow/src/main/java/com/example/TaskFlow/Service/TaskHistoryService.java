package com.example.TaskFlow.Service;

import com.example.TaskFlow.Entity.TaskHistory;
import com.example.TaskFlow.Enum.NotificationStatus;
import com.example.TaskFlow.Repository.TaskHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskHistoryService {

    private final TaskHistoryRepository taskHistoryRepository;

    public List<TaskHistory> getAllTaskHistories(Long taskId) {
        return taskHistoryRepository.findAllByTaskId(taskId);
    }

    public void createTaskHistory(Long taskId, NotificationStatus action, Long performedBy, Map<String, Object> details) {
        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setTaskId(taskId);
        taskHistory.setAction(action);
        taskHistory.setPerformedBy(performedBy);
        taskHistory.setDetails(details);
        taskHistoryRepository.save(taskHistory);
        log.info("TaskHistory created: {} {} {}",
                taskHistory.getTaskId(),
                taskHistory.getAction(),
                taskHistory.getPerformedBy());
    }
}
