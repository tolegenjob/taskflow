package com.example.TaskFlow.Repository;

import com.example.TaskFlow.Entity.TaskHistory;
import com.example.TaskFlow.Enum.NotificationStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
public class TaskHistoryRepositoryTest {

    @Autowired
    private TaskHistoryRepository taskHistoryRepository;

    private final Long taskId1 = 1L;
    private final Long taskId2 = 2L;

    @AfterEach
    void tearDown() {
        taskHistoryRepository.deleteAll();
    }

    @Test
    void findAllByTaskId_success() {
        TaskHistory history1 = new TaskHistory();
        history1.setTaskId(taskId1);
        history1.setAction(NotificationStatus.valueOf("CREATE"));

        TaskHistory history2 = new TaskHistory();
        history2.setTaskId(taskId1);
        history2.setAction(NotificationStatus.valueOf("UPDATE"));

        TaskHistory history3 = new TaskHistory();
        history3.setTaskId(taskId2);
        history3.setAction(NotificationStatus.valueOf("CREATE"));

        taskHistoryRepository.saveAll(List.of(history1, history2, history3));

        List<TaskHistory> result = taskHistoryRepository.findAllByTaskId(taskId1);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(h -> h.getTaskId().equals(taskId1)));
    }

    @Test
    void findAllByTaskId_success_whenNoMatches() {
        TaskHistory history = new TaskHistory();
        history.setTaskId(taskId1);
        taskHistoryRepository.save(history);

        List<TaskHistory> result = taskHistoryRepository.findAllByTaskId(taskId2);

        assertTrue(result.isEmpty());
    }
}
