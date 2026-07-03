package com.rana.notification_system.controller;

import com.rana.notification_system.dto.NotificationLogResponseDTO;
import com.rana.notification_system.dto.NotificationRequestDTO;
import com.rana.notification_system.dto.NotificationResponseDTO;
import com.rana.notification_system.service.NotificationService;
import com.rana.notification_system.service.NotificationServiceImple;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Notification APIs", description = "Operations for creating, retrieving and tracking notifications.")
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(
            summary = "Create a notification",
            description = "Creates a new notification, generates AI-based content, classifies its priority, stores it in the database, and publishes an event to AWS SNS for asynchronous delivery."
    )
    @PostMapping
    public ResponseEntity<NotificationResponseDTO> sendNotification(@Valid @RequestBody NotificationRequestDTO request) {
        NotificationResponseDTO response = notificationService.sendNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get notification by ID",
            description = "Retrieves the details of a notification using its unique identifier."
    )
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> getNotificationById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    @Operation(
            summary = "Get notifications for a user",
            description = "Returns all notifications associated with the specified user."
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userId));
    }

    @Operation(
            summary = "Get notification delivery logs",
            description = "Returns the delivery history and status logs for a specific notification."
    )
    // Get logs for a specific notification
    @GetMapping("/{id}/logs")
    public ResponseEntity<List<NotificationLogResponseDTO>> getLogsByNotificationId(
            @PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getLogsByNotificationId(id));
    }

    @Operation(
            summary = "Get failed notifications",
            description = "Returns all notifications whose delivery status is marked as FAILED."
    )
    // Get all failed notifications
    @GetMapping("/failed")
    public ResponseEntity<List<NotificationResponseDTO>> getFailedNotifications() {
        return ResponseEntity.ok(notificationService.getFailedNotifications());
    }

}
