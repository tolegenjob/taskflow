package com.example.TaskFlow.Controller;

import com.example.TaskFlow.DTO.Response.LogEntryResponse;
import com.example.TaskFlow.Enum.LogLevel;
import com.example.TaskFlow.Service.LogEntryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/logs")
public class LogEntryController {

    private final LogEntryService logEntryService;

    @GetMapping
    public ResponseEntity<Page<LogEntryResponse>> getAllLogEntries(
            @PageableDefault(sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "level", required = false) String level
    ) {
        Page<LogEntryResponse> logEntries = (level == null)
                ? logEntryService.getAllLogEntries(pageable)
                .map(LogEntryResponse::toDTO)
                :
                logEntryService
                        .getAllLogEntriesByLevel(pageable, LogLevel.valueOf(level))
                        .map(LogEntryResponse::toDTO);
        return ResponseEntity.ok(logEntries);
    }

}
