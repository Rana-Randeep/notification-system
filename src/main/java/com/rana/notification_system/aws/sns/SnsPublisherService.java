package com.rana.notification_system.aws.sns;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.ResourceTransactionManager;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

import java.util.HashMap;
import java.util.Map;

@Service
public class SnsPublisherService {

    private final SnsClient snsClient;


    @Value("${aws.sns.topicArn}")
    private String topicArn;

    public SnsPublisherService(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    public String publishMessage(String message, String subject,String type) {
        try {
            Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
            messageAttributes.put("type", MessageAttributeValue.builder()
                    .dataType("String")
                    .stringValue(type)
                    .build());

            PublishRequest request = PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(message)
                    .subject(subject)
                    .messageAttributes(messageAttributes)
                    .build();

            PublishResponse response = snsClient.publish(request);
            System.out.println("Message Published to SNS. MessageId: " + response.messageId());
            return response.messageId();
        }

        catch(SnsException e) {
            System.err.println("failed to publish to SNS: " + e.getMessage());
            throw new RuntimeException("SNS publish failed: " + e.getMessage());
        }
    }
}
