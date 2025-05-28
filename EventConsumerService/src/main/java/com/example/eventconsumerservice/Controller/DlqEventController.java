package com.example.eventconsumerservice.Controller;

import com.example.eventconsumerservice.DTO.Response.DlqEventResponse;
import com.example.eventconsumerservice.Service.DlqEventService;
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
@RequestMapping("/errors")
public class DlqEventController {

    private final DlqEventService dlqEventService;

    @GetMapping
    public ResponseEntity<Page<DlqEventResponse>> getAllEventLogs(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<DlqEventResponse> dlqEventResponses = dlqEventService
                .getAllDlqEvents(pageable)
                .map(DlqEventResponse::toDTO);
        return ResponseEntity.ok(dlqEventResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DlqEventResponse> getEventLogById(@PathVariable String id) {
        DlqEventResponse dlqEventResponse = DlqEventResponse.toDTO(dlqEventService.getDlqEventById(id));
        return ResponseEntity.ok(dlqEventResponse);
    }

}
