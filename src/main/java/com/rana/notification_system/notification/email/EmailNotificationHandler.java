package com.rana.notification_system.notification.email;

import com.rana.notification_system.dto.NotificationPayload;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationHandler {

    private final JavaMailSender mailSender;

    public EmailNotificationHandler(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean sendEmail(NotificationPayload payload) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(payload.getEmail());
            mailMessage.setSubject(payload.getSubject());
            mailMessage.setText(payload.getMessage());

            mailSender.send(mailMessage);

            System.out.println("[EMAIL] Successfully sent to: "
                    + payload.getEmail());
            return true;

        } catch (Exception e) {
            System.err.println("[EMAIL] Failed to send to: "
                    + payload.getEmail()
                    + " | Reason: " + e.getMessage());
            return false;
        }
    }

}
