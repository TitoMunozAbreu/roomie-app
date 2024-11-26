package es.roomie.task.kafka;

public record NotificationMessage(
        String title,
        String description,
        String memberEmail,
        String taskName,
        String taskDueDate
) {
}
