package com.rana.notification_system.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI notificationSystemOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                .title("Notification System API")
                .description("""
                                REST APIs for an AI-powered Notification System
                                built using Spring Boot, AWS SNS, AWS SQS,
                                MySQL and Spring AI.
                                """)
                .version("v1.0")

                .contact(new Contact()
                        .name("Randeep")
                        .url("https://github.com/Rana-Randeep")
                        .email("ranarandeep521@gmail.com"))

                .license(new License()
                        .name("MIT")))

                .externalDocs(new ExternalDocumentation()
                .description("Project Repository")
                .url("https://github.com/Rana-Randeep/notification-system"));

    }
}
