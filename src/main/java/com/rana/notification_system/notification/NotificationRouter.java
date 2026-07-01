package com.rana.notification_system.notification;

import com.rana.notification_system.dto.NotificationPayload;
import com.rana.notification_system.enums.NotificationType;
import com.rana.notification_system.notification.email.EmailNotificationHandler;
import com.rana.notification_system.notification.push.PushNotificationHandler;
import com.rana.notification_system.notification.sms.SmsNotificationHandler;
import com.rana.notification_system.service.DeliveryTrackingService;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;

@Service
public class NotificationRouter {

    private final EmailNotificationHandler emailHandler;
    private final SmsNotificationHandler smsHandler;
    private final PushNotificationHandler pushHandler;
    private final DeliveryTrackingService deliveryTrackingService;

    public NotificationRouter(EmailNotificationHandler emailHandler,
                              SmsNotificationHandler smsHandler,
                              PushNotificationHandler pushHandler, DeliveryTrackingService deliveryTrackingService) {
        this.emailHandler = emailHandler;
        this.smsHandler = smsHandler;
        this.pushHandler = pushHandler;
        this.deliveryTrackingService = deliveryTrackingService;
    }

    public boolean route(NotificationPayload payload) {
        String type = payload.getType();
        boolean success;
        NotificationType channel;

        switch (type) {
            case "EMAIL":
                channel = NotificationType.EMAIL;
                success = emailHandler.sendEmail(payload);
                break;
            case "SMS":
                channel = NotificationType.SMS;
                success = smsHandler.sendSms(payload);
                break;
            case "PUSH":
                channel = NotificationType.PUSH;
                success = pushHandler.sendPush(payload);
                break;
            default:
                System.err.println("[ROUTER] Unknown notification type: " + type);
                return false;
        }

        // Track delivery result in DB
        deliveryTrackingService.updateDeliveryStatus(
                payload.getNotificationId(),
                channel,
                success
        );

        return success;
    }
}
