package com.rana.notification_system.entity;

import com.rana.notification_system.enums.DeliveryStatus;
import com.rana.notification_system.enums.NotificationType;
import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity
@Table(name="notification_logs")
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="notification_id", nullable = false)
    private Notification notification;

    @Enumerated(EnumType.STRING)
    @Column(name="channel", nullable = false)
    private NotificationType channel;


    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeliveryStatus status;

    @Column(name="response_message")
    private String responseMessage;

    @Column(name="attempted_at")
    private LocalDateTime attemptedAt;

    public NotificationLog(){}

    public NotificationLog(Long id, Notification notification, NotificationType channel, DeliveryStatus status, String responseMessage, LocalDateTime attemptedAt) {
        this.id = id;
        this.notification = notification;
        this.channel = channel;
        this.status = status;
        this.responseMessage = responseMessage;
        this.attemptedAt = attemptedAt;
    }

    //Getters

    public Long getId() {
        return id;
    }

    public Notification getNotification() {
        return notification;
    }

    public NotificationType getChannel() {
        return channel;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public LocalDateTime getAttemptedAt() {
        return attemptedAt;
    }

    //Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public void setChannel(NotificationType channel) {
        this.channel = channel;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public void setAttemptedAt(LocalDateTime attemptedAt) {
        this.attemptedAt = attemptedAt;
    }
}
