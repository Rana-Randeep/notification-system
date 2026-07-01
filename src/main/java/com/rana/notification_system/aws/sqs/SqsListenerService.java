package com.rana.notification_system.aws.sqs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rana.notification_system.dto.NotificationPayload;
import com.rana.notification_system.exception.NotificationProcessingException;
import com.rana.notification_system.exception.ResourceNotFoundException;
import com.rana.notification_system.notification.NotificationRouter;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;

@Service
public class SqsListenerService {
    private final SqsClient sqsClient;

    private final NotificationRouter notificationRouter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${aws.sqs.emailQueueUrl}")
    private String emailQueueUrl;

    @Value("${aws.sqs.smsQueueUrl}")
    private String smsQueueUrl;

    @Value("${aws.sqs.pushQueueUrl}")
    private String pushQueueUrl;

    public SqsListenerService(SqsClient sqsClient, NotificationRouter notificationRouter) {
        this.sqsClient = sqsClient;
        this.notificationRouter = notificationRouter;
    }

    @PostConstruct
    public void startListeners() {
        new Thread(() -> listen(emailQueueUrl, "EMAIL"), "email-listener").start();
        new Thread(() -> listen(smsQueueUrl, "SMS"), "sms-listener").start();
        new Thread(() -> listen(pushQueueUrl, "PUSH"), "push-listener").start();
    }

    private void listen(String queueUrl, String channel) {
        System.out.println("[" + channel + "] Listener started");

        while (true) {
            try {
                ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .maxNumberOfMessages(5)
                        .waitTimeSeconds(10) //Long polling
                        .build();

                List<Message> messages = sqsClient
                        .receiveMessage(receiveRequest)
                        .messages();

                for (Message message : messages) {
                    System.out.println("[" + channel + "] Received message: "
                            + message.body());

                    processMessage(message.body(), channel);

                    deleteMessage(queueUrl, message.receiptHandle());
                }

            }
            catch (Exception e) {
                System.err.println("[" + channel + "] Listener error: "
                        + e.getMessage());

                try {
                    Thread.sleep(30000);
                }
                catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    private void processMessage(String messageBody, String channel) {
        try {
            // Step 1 — Parse SNS envelope
            JsonNode snsEnvelope = objectMapper.readTree(messageBody);

            // Step 2 — Extract inner "Message" field
            String innerMessage = snsEnvelope.get("Message").asText();
            System.out.println("[" + channel + "] Inner payload: " + innerMessage);

            // Step 3 — Parse inner message into NotificationPayload
            NotificationPayload payload = objectMapper
                    .readValue(innerMessage, NotificationPayload.class);

            // Step 4 — Route to correct handler
            boolean success = notificationRouter.route(payload);

            if (success) {
                System.out.println("[" + channel + "] Notification processed successfully.");
            } else {
                System.err.println("[" + channel + "] Notification processing failed.");
            }

        } catch (ResourceNotFoundException e) {
            // Log but don't retry — resource genuinely missing
            System.err.println("[" + channel + "] Resource not found: "
                    + e.getMessage());

        } catch (Exception e) {
            System.err.println("[" + channel + "] Error processing message: "
                    + e.getMessage());
            throw new NotificationProcessingException(
                    "Failed to process " + channel + " notification: "
                            + e.getMessage());
        }
    }

    private void deleteMessage(String queueUrl, String receiptHandle) {
        DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(receiptHandle)
                .build();

        sqsClient.deleteMessage(deleteRequest);
        System.out.println("Message deleted from queue.");
    }

}
