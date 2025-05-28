package com.example.TaskFlow.Controller;

import com.example.TaskFlow.DTO.Response.EventResponse;
import com.example.TaskFlow.Service.EventLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventLogController {

    private final EventLogService eventLogService;

    @GetMapping
    public ResponseEntity<Page<EventResponse>> getAllEventLogs(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<EventResponse> eventResponses = eventLogService
                .getAllEventLogs(pageable)
                .map(EventResponse::toDTO);
        return ResponseEntity.ok(eventResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventLogById(@PathVariable String id) {
        EventResponse eventResponse = EventResponse.toDTO(eventLogService.getEventLogById(id));
        return ResponseEntity.ok(eventResponse);
    }
}
