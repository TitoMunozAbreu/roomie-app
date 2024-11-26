package es.roomie.notification.kafka.task;

import java.time.LocalDateTime;

public record TaskNotificationMessage(
        String title,
        String description,
        String memberEmail,
        String taskName,
        String taskDueDate
) {
}
