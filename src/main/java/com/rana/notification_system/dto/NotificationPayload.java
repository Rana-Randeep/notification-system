package com.rana.notification_system.dto;

public class NotificationPayload {

    private Long notificationId;
    private Long userId;
    private String type;
    private String subject;
    private String message;
    private String email;
    private String phoneNumber;

    public NotificationPayload() {}

    public NotificationPayload(Long notificationId, Long userId, String type,
                               String subject, String message,
                               String email, String phoneNumber) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.type = type;
        this.subject = subject;
        this.message = message;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    //Getters
    public Long getNotificationId() { return notificationId; }
    public Long getUserId() { return userId; }
    public String getType() { return type; }
    public String getSubject() { return subject; }
    public String getMessage() { return message; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }


    //Setters
    public void setNotificationId(Long notificationId) { this.notificationId = notificationId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setType(String type) { this.type = type; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setMessage(String message) { this.message = message; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

}
