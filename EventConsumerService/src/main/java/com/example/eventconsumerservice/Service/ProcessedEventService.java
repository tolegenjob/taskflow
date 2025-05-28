package com.example.eventconsumerservice.Service;

import com.example.eventconsumerservice.DTO.Event.IncomeEvent;
import com.example.eventconsumerservice.Entity.ProcessedEvent;
import com.example.eventconsumerservice.Repository.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.example.eventconsumerservice.Util.EntityUtil.findOrThrow;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProcessedEventService {

    private final ProcessedEventRepository processedEventRepository;

    public void createProcessedEvent(IncomeEvent event) {
        ProcessedEvent processedEvent = new ProcessedEvent();
        processedEvent.setEventType(event.eventType());
        processedEvent.setEntityId(event.entityId());
        processedEvent.setEntityType(event.entityType());
        processedEvent.setPayload(event.payload());
        ProcessedEvent saved = processedEventRepository.save(processedEvent);
        log.info("Created ProcessedEvent with id: {}", saved.getId());
    }

    public Page<ProcessedEvent> getAllProcessedEvents(Pageable pageable) {
        log.info("Got all ProcessedEvents");
        return processedEventRepository.findAll(pageable);
    }

    public ProcessedEvent getProcessedEventById(String id) {
        ObjectId dlqEventId = new ObjectId(id);
        log.info("Got ProcessedEvent by id: {}", id);
        return findOrThrow(processedEventRepository, dlqEventId, "ProcessedEvent");
    }

}
