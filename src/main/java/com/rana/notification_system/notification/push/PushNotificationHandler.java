package com.rana.notification_system.notification.push;

import com.rana.notification_system.dto.NotificationPayload;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationHandler {

    public boolean sendPush(NotificationPayload payload) {
        try {
            // Firebase FCM integration goes here in production
            // For now → simulating Push delivery
            System.out.println("[PUSH] Sending to userId: "
                    + payload.getUserId());
            System.out.println("[PUSH] Subject: "
                    + payload.getSubject());
            System.out.println("[PUSH] Message: "
                    + payload.getMessage());
            System.out.println("[PUSH] Successfully sent to userId: "
                    + payload.getUserId());
            return true;

        } catch (Exception e) {
            System.err.println("[PUSH] Failed for userId: "
                    + payload.getUserId()
                    + " | Reason: " + e.getMessage());
            return false;
        }
    }
}
