package es.roomie.notification.kafka.task;

public record TaskNotificationMessage(
        String title,
        String description,
        String memberEmail,
        String taskName,
        String taskDueDate
) {
}
