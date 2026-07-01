package com.rana.notification_system.service;

import com.rana.notification_system.dto.NotificationLogResponseDTO;
import com.rana.notification_system.dto.NotificationRequestDTO;
import com.rana.notification_system.dto.NotificationResponseDTO;

import java.util.List;

public interface NotificationService {

    NotificationResponseDTO sendNotification(NotificationRequestDTO request);

    List<NotificationResponseDTO> getNotificationsByUserId(Long userId);

    NotificationResponseDTO getNotificationById(Long id);

    List<NotificationLogResponseDTO> getLogsByNotificationId(Long notificationId);

    List<NotificationResponseDTO> getFailedNotifications();
}
