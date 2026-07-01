package com.rana.notification_system.repository;

import com.rana.notification_system.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
    List<NotificationLog> findByNotificationId(Long notificationId);
}
