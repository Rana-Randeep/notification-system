# Architecture Decisions

This document explains the major design decisions made while building the Notification System. Rather than describing technologies in general, it focuses on **why** each decision was made, the alternatives considered, and the trade-offs involved.

---

# 1. Why Layered Architecture?

### Decision

The project follows a layered architecture:

```text
Controller → Service → Repository
```

### Why

The application handles multiple responsibilities—processing HTTP requests, generating AI content, publishing events, interacting with the database, and delivering notifications. Separating these concerns keeps each layer focused on a single responsibility and makes the code easier to understand.

### Alternative

Controllers could directly interact with repositories and AWS services.

### Trade-off

Layered architecture introduces more classes but results in cleaner code that is easier to maintain and extend.


---

# 2. Why AWS SNS?

### Decision

AWS SNS is used to publish notification events.

### Why

When a notification is created, the service shouldn't be responsible for sending Email, SMS, and Push notifications individually. Instead, it publishes a single event and allows other components to decide how that event should be processed.

This keeps notification creation independent from notification delivery.

### Alternative

The Notification Service could directly invoke every notification handler.

### Trade-off

SNS introduces another AWS service to manage, but significantly reduces coupling between components.

### Real Project Insight

While testing, I discovered that every SQS queue was receiving every notification because subscription filter policies were missing. After adding message attributes and configuring filter policies, each queue received only the notifications intended for its channel.

---

# 3. Why AWS SQS?

### Decision

Each notification channel consumes messages from its own SQS queue.

### Why

Sending Email, SMS, and Push notifications can take time. Processing them asynchronously prevents the API request from waiting for delivery to complete.

Each notification channel can process messages independently without affecting the others.

### Alternative

Deliver notifications synchronously inside the API request.

### Trade-off

Queue-based processing increases infrastructure complexity but improves responsiveness and isolates failures between notification channels.

---

# 4. Why Separate Notification Handlers?

### Decision

Email, SMS, and Push notifications are implemented as separate handlers.

### Why

Each notification channel has different delivery logic. Keeping them separate avoids large conditional blocks and makes it easier to modify one notification channel without affecting the others.

### Alternative

A single service containing multiple `if-else` or `switch` statements.

### Trade-off

More classes are created, but each class has a single responsibility and is easier to maintain.

---

# 5. Why Spring AI?

### Decision

Spring AI (Ollama) is used to generate notification content and classify notification priority.

### Why

Instead of relying entirely on predefined templates, the system can generate context-aware notification messages while also classifying their priority before processing.

### Alternative

Store fixed templates and manually assign notification priority.

### Trade-off

AI introduces additional processing time and an external dependency, but provides more flexibility and demonstrates how AI can be integrated into backend applications.

---

# 6. Why DTOs?

### Decision

REST APIs use DTOs instead of exposing JPA entities directly.

### Why

Database entities represent the persistence model, while DTOs define the API contract. Keeping them separate prevents internal database changes from affecting API responses.

### Alternative

Return JPA entities directly from controller methods.

### Trade-off

DTOs require additional mapping but provide better control over the data exposed through APIs.

---

# 7. Why Spring Data JPA?

### Decision

Database operations are handled through Spring Data JPA repositories.

### Why

Most database operations in this project are standard CRUD operations. Spring Data JPA reduces boilerplate code and keeps persistence logic separate from business logic.

### Alternative

Write SQL queries manually using JDBC.

### Trade-off

Spring Data JPA abstracts many database operations, which improves development speed, although complex queries may still require custom JPQL or native SQL.

---

# 8. Why Track Notification Status?

### Decision

Notification status and delivery logs are stored in MySQL.

### Why

AWS SNS and SQS manage message delivery between services, but they do not represent the business state of a notification. Persisting notification status allows the application to track whether a notification is pending, sent, or failed.

### Alternative

Rely only on AWS messaging services without maintaining delivery history.

### Trade-off

The application performs additional database writes but gains complete notification history for debugging and reporting.

---

# 9. Why Global Exception Handling?

### Decision

The project uses a centralized exception handler.

### Why

Handling exceptions in one place keeps controllers focused on business functionality while ensuring consistent error responses across all APIs.

### Alternative

Handle exceptions separately inside every controller.

### Trade-off

A dedicated exception handler introduces another class but significantly reduces duplicated error-handling code.

---

# 10. Why Externalized Configuration?

### Decision

Sensitive configuration is stored using environment variables instead of hardcoding credentials.

### Why

AWS credentials, mail credentials, and database passwords should never be committed to version control. Environment variables keep sensitive information outside the codebase while allowing different environments to use different configurations.

### Alternative

Store credentials directly inside `application.properties`.

### Trade-off

Initial project setup requires additional configuration, but the repository remains secure and portable.

---

# Lessons Learned

Building this project taught me that designing a backend system involves much more than implementing APIs. Small architectural decisions—such as separating notification publishing from delivery, keeping responsibilities isolated, and externalizing configuration—have a significant impact on maintainability, scalability, and security.

One of the most valuable lessons came while debugging the SNS to SQS integration. A missing subscription filter policy caused every queue to receive every notification. Tracing that issue from application logs to AWS configuration reinforced the importance of validating infrastructure behavior instead of assuming managed services are configured correctly.
