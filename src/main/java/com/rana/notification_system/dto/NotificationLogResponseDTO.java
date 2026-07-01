package com.rana.notification_system.dto;

import com.rana.notification_system.enums.DeliveryStatus;
import com.rana.notification_system.enums.NotificationType;

import java.time.LocalDateTime;

public class NotificationLogResponseDTO {

    private Long id;
    private Long notificationId;
    private NotificationType channel;
    private DeliveryStatus status;
    private String responseMessage;
    private LocalDateTime attemptedAt;

    public NotificationLogResponseDTO() {}

    public NotificationLogResponseDTO(Long id, Long notificationId,
                                      NotificationType channel,
                                      DeliveryStatus status,
                                      String responseMessage,
                                      LocalDateTime attemptedAt) {
        this.id = id;
        this.notificationId = notificationId;
        this.channel = channel;
        this.status = status;
        this.responseMessage = responseMessage;
        this.attemptedAt = attemptedAt;
    }

    public Long getId() { return id; }
    public Long getNotificationId() { return notificationId; }
    public NotificationType getChannel() { return channel; }
    public DeliveryStatus getStatus() { return status; }
    public String getResponseMessage() { return responseMessage; }
    public LocalDateTime getAttemptedAt() { return attemptedAt; }

    public void setId(Long id) { this.id = id; }
    public void setNotificationId(Long notificationId) { this.notificationId = notificationId; }
    public void setChannel(NotificationType channel) { this.channel = channel; }
    public void setStatus(DeliveryStatus status) { this.status = status; }
    public void setResponseMessage(String responseMessage) { this.responseMessage = responseMessage; }
    public void setAttemptedAt(LocalDateTime attemptedAt) { this.attemptedAt = attemptedAt; }
}
