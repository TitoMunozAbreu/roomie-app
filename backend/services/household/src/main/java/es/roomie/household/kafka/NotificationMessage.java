package es.roomie.household.kafka;

public record NotificationMessage(
        String title,
        String description,
        String memberEmail
) {
}
