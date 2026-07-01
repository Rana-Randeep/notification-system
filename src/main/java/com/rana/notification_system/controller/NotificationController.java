package com.rana.notification_system.controller;

import com.rana.notification_system.dto.NotificationLogResponseDTO;
import com.rana.notification_system.dto.NotificationRequestDTO;
import com.rana.notification_system.dto.NotificationResponseDTO;
import com.rana.notification_system.service.NotificationService;
import com.rana.notification_system.service.NotificationServiceImple;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationResponseDTO> sendNotification(@Valid @RequestBody NotificationRequestDTO request) {
        NotificationResponseDTO response = notificationService.sendNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> getNotificationById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userId));
    }

    // Get logs for a specific notification
    @GetMapping("/{id}/logs")
    public ResponseEntity<List<NotificationLogResponseDTO>> getLogsByNotificationId(
            @PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getLogsByNotificationId(id));
    }

    // Get all failed notifications
    @GetMapping("/failed")
    public ResponseEntity<List<NotificationResponseDTO>> getFailedNotifications() {
        return ResponseEntity.ok(notificationService.getFailedNotifications());
    }

}
