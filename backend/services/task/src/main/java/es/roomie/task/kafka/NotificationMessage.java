package es.roomie.task.kafka;

/**
 * A record class representing a notification message.
 * This class is immutable and contains details about a task notification.
 *
 * @param title          The title of the notification message.
 * @param description    A brief description of the notification.
 * @param memberEmail    The email of the member to whom the notification is addressed.
 * @param taskName       The name of the task related to the notification.
 * @param taskDueDate    The due date of the task in the format 'YYYY-MM-DD'.
 */
public record NotificationMessage(
        String title,
        String description,
        String memberEmail,
        String taskName,
        String taskDueDate
) {
}
