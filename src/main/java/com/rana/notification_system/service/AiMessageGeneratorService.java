package com.rana.notification_system.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiMessageGeneratorService {
    private final ChatClient chatClient;

    public AiMessageGeneratorService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String generateMessage(String userName,
                                  String subject,
                                  String notificationType) {
        String prompt = "You are a notification message writer for a"
                + " professional application. "
                + "Write a short, friendly and professional notification"
                + " message for the following: "
                + "User Name: " + userName + ". "
                + "Notification Subject: " + subject + ". "
                + "Notification Type: " + notificationType + ". "
                + "Keep the message under 3 sentences. "
                + "Do not include subject line. "
                + "Just write the message body only.";

        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        System.out.println("[AI] Generated message: " + response);
        return response;
    }
}
