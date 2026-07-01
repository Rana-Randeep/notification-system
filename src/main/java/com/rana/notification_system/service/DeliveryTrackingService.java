package com.rana.notification_system.service;

import com.rana.notification_system.entity.Notification;
import com.rana.notification_system.entity.NotificationLog;
import com.rana.notification_system.enums.DeliveryStatus;
import com.rana.notification_system.enums.NotificationType;
import com.rana.notification_system.repository.NotificationLogRepository;
import com.rana.notification_system.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeliveryTrackingService {

    private final NotificationRepository notificationRepository;
    private final NotificationLogRepository notificationLogRepository;

    public DeliveryTrackingService(NotificationRepository notificationRepository,
                                   NotificationLogRepository notificationLogRepository) {
        this.notificationRepository = notificationRepository;
        this.notificationLogRepository = notificationLogRepository;
    }

    public void updateDeliveryStatus(Long notificationId,
                                     NotificationType channel,
                                     boolean success) {

        // Step 1 — Fetch notification from DB
        Notification notification = notificationRepository
                .findById(notificationId)
                .orElse(null);

        if (notification == null) {
            System.err.println("[TRACKING] Notification not found: "
                    + notificationId);
            return;
        }

        // Step 2 — Determine status
        DeliveryStatus status = success
                ? DeliveryStatus.SENT
                : DeliveryStatus.FAILED;

        String responseMessage = success
                ? "Notification delivered successfully"
                : "Notification delivery failed";

        // Step 3 — Update notification status
        notification.setStatus(status);
        notification.setUpdatedAt(LocalDateTime.now());
        notificationRepository.save(notification);

        System.out.println("[TRACKING] Notification " + notificationId
                + " status updated to: " + status);

        // Step 4 — Create notification log entry
        NotificationLog log = new NotificationLog();
        log.setNotification(notification);
        log.setChannel(channel);
        log.setStatus(status);
        log.setResponseMessage(responseMessage);
        log.setAttemptedAt(LocalDateTime.now());

        notificationLogRepository.save(log);

        System.out.println("[TRACKING] Log created for notificationId: "
                + notificationId + " | channel: " + channel);
    }
}
