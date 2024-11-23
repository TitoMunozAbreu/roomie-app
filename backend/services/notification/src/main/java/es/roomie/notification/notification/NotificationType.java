package es.roomie.notification.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationType {
    NEW_MEMBER_CONFIRMATION,
    HOUSEHOLD_CONFIRMATION,
}
