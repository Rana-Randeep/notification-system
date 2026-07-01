package com.rana.notification_system.dto;

import com.rana.notification_system.enums.DeliveryStatus;
import com.rana.notification_system.enums.NotificationType;

import java.time.LocalDateTime;

public class NotificationResponseDTO {

    private Long id;
    private Long userId;
    private NotificationType type;
    private String subject;
    private String message;
    private DeliveryStatus status;
    private LocalDateTime createdAt;
    private String priority;

    public NotificationResponseDTO() {};

    public NotificationResponseDTO(Long id, Long userId, NotificationType type, String subject, String message, DeliveryStatus status, String priority, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.subject = subject;
        this.message = message;
        this.status = status;
        this.priority = priority;
        this.createdAt = createdAt;
    }

    //Getters

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public NotificationType getType() {
        return type;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public String getPriority() {
        return priority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    //Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
