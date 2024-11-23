package es.roomie.notification.kafka.household;

public record NotificationMessage(
        String title,
        String description,
        String memberEmail
) {}
