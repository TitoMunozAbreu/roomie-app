package es.roomie.household.kafka;

/**
 * Represents a notification message in the system.
 * This record holds the details of a notification including its title,
 * description, and the member's email address to whom the notification is directed.
 *
 * @param title The title of the notification message.
 * @param description A detailed description of the notification message.
 * @param memberEmail The email address of the member receiving the notification.
 */
public record NotificationMessage(
        String title,
        String description,
        String memberEmail
) {
}
