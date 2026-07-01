package com.rana.notification_system.repository;

import com.rana.notification_system.entity.Notification;
import com.rana.notification_system.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
    List<Notification> findByStatus(DeliveryStatus status);
}
