package com.example.eventconsumerservice.Controller;

import com.example.eventconsumerservice.DTO.Response.ProcessedEventResponse;
import com.example.eventconsumerservice.Service.ProcessedEventService;
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
@RequestMapping("/processed-events")
public class ProcessedEventController {

    private final ProcessedEventService processedEventService;

    @GetMapping
    public ResponseEntity<Page<ProcessedEventResponse>> getAllEventLogs(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ProcessedEventResponse> processedEventResponses = processedEventService
                .getAllProcessedEvents(pageable)
                .map(ProcessedEventResponse::toDTO);
        return ResponseEntity.ok(processedEventResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcessedEventResponse> getEventLogById(@PathVariable String id) {
        ProcessedEventResponse processedEventResponse = ProcessedEventResponse
                .toDTO(processedEventService.getProcessedEventById(id));
        return ResponseEntity.ok(processedEventResponse);
    }

}
