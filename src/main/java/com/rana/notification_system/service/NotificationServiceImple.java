package com.rana.notification_system.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rana.notification_system.aws.sns.SnsPublisherService;
import com.rana.notification_system.dto.NotificationLogResponseDTO;
import com.rana.notification_system.dto.NotificationRequestDTO;
import com.rana.notification_system.dto.NotificationResponseDTO;
import com.rana.notification_system.entity.Notification;
import com.rana.notification_system.entity.NotificationLog;
import com.rana.notification_system.entity.User;
import com.rana.notification_system.enums.DeliveryStatus;
import com.rana.notification_system.exception.ResourceNotFoundException;
import com.rana.notification_system.repository.NotificationLogRepository;
import com.rana.notification_system.repository.NotificationRepository;
import com.rana.notification_system.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImple implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SnsPublisherService snsPublisherService;
    private final NotificationLogRepository notificationLogRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AiMessageGeneratorService aiMessageGeneratorService;
    private final AiPriorityClassifierService aiPriorityClassifierService;


    public NotificationServiceImple(NotificationRepository notificationRepository, UserRepository userRepository, SnsPublisherService snsPublisherService, NotificationLogRepository notificationLogRepository, AiMessageGeneratorService aiMessageGeneratorService, AiPriorityClassifierService aiPriorityClassifierService) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.snsPublisherService = snsPublisherService;
        this.notificationLogRepository = notificationLogRepository;
        this.aiMessageGeneratorService = aiMessageGeneratorService;
        this.aiPriorityClassifierService = aiPriorityClassifierService;
    }

    public NotificationResponseDTO sendNotification(NotificationRequestDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->  new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        // Step 1 — AI generates personalized message
        String aiGeneratedMessage = aiMessageGeneratorService.generateMessage(
                user.getName(),
                request.getSubject(),
                request.getType().toString()
        );

//        // Step 2 — AI classifies priority
        String priority = aiPriorityClassifierService.classifyPriority(
                request.getSubject(),
                aiGeneratedMessage
        );

        //  Step 3 — Build and save notification
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(request.getType());
        notification.setSubject(request.getSubject());
        notification.setMessage(aiGeneratedMessage);
        notification.setStatus(DeliveryStatus.PENDING);
        notification.setPriority(priority);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUpdatedAt(LocalDateTime.now());

        Notification saved = notificationRepository.save(notification);

        // Step 4 — Publish to SNS
        //Build message payload for SNS
        String snsMesaage = buildSnsMessage(saved, user);
        snsPublisherService.publishMessage(snsMesaage, request.getSubject(), request.getType().toString());

        return mapToResponse(saved);
    }

    //Helper Method
    private String buildSnsMessage(Notification notification, User user) {
        try {
            java.util.Map<String, Object> map = new java.util.LinkedHashMap<>();
            map.put("notificationId", notification.getId());
            map.put("userId", user.getId());
            map.put("type", notification.getType().toString());
            map.put("subject", notification.getSubject());
            map.put("message", notification.getMessage());
            map.put("email", user.getEmail());
            map.put("phoneNumber", user.getPhoneNumber());
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            throw new RuntimeException("Failed to build SNS message: "
                    + e.getMessage());
        }
    }

    public List<NotificationResponseDTO> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }

    public NotificationResponseDTO getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        return mapToResponse(notification);
    }

    public NotificationResponseDTO mapToResponse(Notification n) {
        return new NotificationResponseDTO(
                n.getId(),
                n.getUser().getId(),
                n.getType(),
                n.getSubject(),
                n.getMessage(),
                n.getStatus(),
                n.getPriority(),
                n.getCreatedAt()
        );
    }

    public List<NotificationLogResponseDTO> getLogsByNotificationId(Long notificationId) {
        return notificationLogRepository
                .findByNotificationId(notificationId)
                .stream()
                .map(this::mapLogToResponse)
                .collect(Collectors.toList());
    }

    public List<NotificationResponseDTO> getFailedNotifications() {
        return notificationRepository
                .findByStatus(DeliveryStatus.FAILED)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private NotificationLogResponseDTO mapLogToResponse(NotificationLog log) {
        return new NotificationLogResponseDTO(
                log.getId(),
                log.getNotification().getId(),
                log.getChannel(),
                log.getStatus(),
                log.getResponseMessage(),
                log.getAttemptedAt()
        );
    }




}
