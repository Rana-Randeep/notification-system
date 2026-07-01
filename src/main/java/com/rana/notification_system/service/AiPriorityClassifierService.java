package com.rana.notification_system.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiPriorityClassifierService {

    private final ChatClient chatClient;

    public AiPriorityClassifierService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String classifyPriority(String subject, String message) {
        String prompt = "You are a notification priority classifier. "
                + "Based on the subject and message below, "
                + "classify the priority as exactly one word: "
                + "HIGH, MEDIUM, or LOW. "
                + "Rules: "
                + "HIGH = OTP, payment, security, urgent, alert, verify. "
                + "MEDIUM = order, update, reminder, confirmation. "
                + "LOW = promotion, sale, newsletter, offer. "
                + "Subject: " + subject + ". "
                + "Message: " + message + ". "
                + "Reply with only one word: HIGH, MEDIUM, or LOW.";

        String priority = chatClient.prompt()
                .user(prompt)
                .call()
                .content()
                .trim()
                .toUpperCase();

        System.out.println("[AI] Classified priority: " + priority);
        return priority;
    }

}
