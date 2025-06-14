package com.example.TaskFlow.Service;

import com.example.TaskFlow.DTO.Event.Event;
import com.example.TaskFlow.Entity.EventLog;
import com.example.TaskFlow.Repository.EventLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.example.TaskFlow.Util.EntityUtil.findOrThrow;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventLogService {

    private final EventLogRepository eventLogRepository;

    public Page<EventLog> getAllEventLogs(Pageable pageable) {
        log.info("Got all EventLogs");
        return eventLogRepository.findAll(pageable);
    }

    public EventLog getEventLogById(String id) {
        ObjectId eventLogId = new ObjectId(id);
        log.info("Got EventLog by id: {}", id);
        return findOrThrow(eventLogRepository, eventLogId, "EventLog");
    }

    public void createEventLog(Event event) {
        EventLog eventLog = new EventLog();
        eventLog.setEventType(event.eventType());
        eventLog.setEntityId(event.entityId());
        eventLog.setEntityType(event.entityType());
        eventLog.setPayload(event.payload());
        EventLog saved = eventLogRepository.save(eventLog);
        log.info("Saved EventLog with id: {}", saved.getId());
    }
}
