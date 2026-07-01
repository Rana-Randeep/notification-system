package com.rana.notification_system.notification.sms;

import com.rana.notification_system.dto.NotificationPayload;
import org.springframework.stereotype.Service;

@Service
public class SmsNotificationHandler {

    public boolean sendSms(NotificationPayload payload) {
        try {
            // Twilio integration goes here in production
            // For now → simulating SMS delivery
            System.out.println("[SMS] Sending to: "
                    + payload.getPhoneNumber());
            System.out.println("[SMS] Message: "
                    + payload.getMessage());
            System.out.println("[SMS] Successfully sent to: "
                    + payload.getPhoneNumber());
            return true;

        } catch (Exception e) {
            System.err.println("[SMS] Failed to send to: "
                    + payload.getPhoneNumber()
                    + " | Reason: " + e.getMessage());
            return false;
        }
    }
}
