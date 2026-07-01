package com.rana.notification_system.dto;

import com.rana.notification_system.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class NotificationRequestDTO {

    @NotNull(message="User ID isRequired")
    private Long userId;

    @NotNull(message="Notification type is required")
    private NotificationType type;

    @NotBlank(message = "Subject is Required")
    private String subject;

    private String message;

    public NotificationRequestDTO() {};

    public NotificationRequestDTO(Long userId, NotificationType type, String subject, String message) {
        this.userId = userId;
        this.type = type;
        this.subject = subject;
        this.message = message;
    }

    //Getters

    public Long getUserId() { return userId; }

    public NotificationType getType() { return type; }

    public String getSubject() { return subject; }

    public String getMessage() { return message; }

    //Setters

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
}
