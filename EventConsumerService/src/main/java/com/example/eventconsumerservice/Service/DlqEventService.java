package com.example.eventconsumerservice.Service;

import com.example.eventconsumerservice.DTO.Event.DlqIncomeEvent;
import com.example.eventconsumerservice.Entity.DlqEvent;
import com.example.eventconsumerservice.Repository.DlqEventRepository;
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
public class DlqEventService {

    private final DlqEventRepository dlqEventRepository;

    public void createDlqEvent(DlqIncomeEvent dlqIncomeEvent) {
        DlqEvent dlqEvent = new DlqEvent();
        dlqEvent.setEventType(dlqIncomeEvent.eventType());
        dlqEvent.setEntityId(dlqIncomeEvent.entityId());
        dlqEvent.setEntityType(dlqIncomeEvent.entityType());
        dlqEvent.setPayload(dlqIncomeEvent.payload());
        dlqEvent.setErrorMessage(dlqIncomeEvent.errorMessage());
        dlqEvent.setFailedAt(dlqIncomeEvent.failedAt());
        DlqEvent saved = dlqEventRepository.save(dlqEvent);
        log.info("Created DlqEvent with id: {}", saved.getId());
    }

    public Page<DlqEvent> getAllDlqEvents(Pageable pageable) {
        log.info("Got all DlqEvents");
        return dlqEventRepository.findAll(pageable);
    }

    public DlqEvent getDlqEventById(String id) {
        ObjectId dlqEventId = new ObjectId(id);
        log.info("Got DlqEvent by id: {}", id);
        return findOrThrow(dlqEventRepository, dlqEventId, "DlqEvent");
    }

}
