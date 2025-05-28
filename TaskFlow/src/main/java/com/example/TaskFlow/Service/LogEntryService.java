package com.example.TaskFlow.Service;

import com.example.TaskFlow.DTO.Event.LogEntryEvent;
import com.example.TaskFlow.Entity.LogEntry;
import com.example.TaskFlow.Enum.LogLevel;
import com.example.TaskFlow.Repository.LogEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogEntryService {

    private final LogEntryRepository logEntryRepository;

    public Page<LogEntry> getAllLogEntries(Pageable pageable) {
        log.info("Got all logEntries");
        return logEntryRepository.findAll(pageable);
    }

    public Page<LogEntry> getAllLogEntriesByLevel(Pageable pageable, LogLevel level) {
        log.info("Got all logEntries by level {}", level);
        return logEntryRepository.findByLevel(pageable, level);
    }

    public void createLogEntry(LogEntryEvent logEntryEvent) {
        LogEntry logEntry = new LogEntry();
        logEntry.setLevel(logEntryEvent.level());
        logEntry.setMessage(logEntryEvent.message());
        logEntry.setContext(logEntryEvent.context());
        LogEntry saved = logEntryRepository.save(logEntry);
        log.info("Saved logEntry with id: {}", saved.getId());
    }

}
