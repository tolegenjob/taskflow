package com.example.notificationservice.Controller;

import com.example.notificationservice.DTO.Response.NotificationResponse;
import com.example.notificationservice.Service.NotificationService;
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
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getAllNotifications(
            @PageableDefault(sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<NotificationResponse> notificationResponses = notificationService.getAllNotifications(pageable)
                .map(NotificationResponse::toDTO);
        return ResponseEntity.ok(notificationResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable Long id) {
        NotificationResponse notificationResponse = NotificationResponse.toDTO(notificationService.getNotificationById(id));
        return ResponseEntity.ok(notificationResponse);
    }

}
