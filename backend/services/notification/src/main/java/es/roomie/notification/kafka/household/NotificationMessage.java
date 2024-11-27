package es.roomie.notification.kafka.household;

/**
 * Represents a notification message in the system.
 * This record encapsulates the data needed to send a notification,
 * including a title, a description of the notification, and the email
 * of the member to whom the notification is directed.
 *
 * @param title       The title of the notification.
 * @param description A detailed description of the notification.
 * @param memberEmail The email address of the member receiving the notification.
 */
public record NotificationMessage(
        String title,
        String description,
        String memberEmail
) {}
