package es.roomie.notification.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing the various types of notifications in the system.
 * This enum is used to categorize different types of notifications that can be sent to users.
 * Each constant in this enum represents a specific notification type.
 */
@AllArgsConstructor
@Getter
public enum NotificationType {
    NEW_MEMBER_CONFIRMATION,
    HOUSEHOLD_CONFIRMATION,
    TASK_CONFIRMATION,
}
