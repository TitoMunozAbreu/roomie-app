package es.roomie.notification.notification;

import es.roomie.notification.kafka.household.NotificationMessage;
import es.roomie.notification.kafka.household.NewMemberInvitation;
import es.roomie.notification.kafka.task.TaskNotificationMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * The Notification class represents a notification entity in the application.
 * It contains various fields that define the properties of a notification,
 * including its type, date, and associated messages.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Notification {
    @Id private String id;
    private NotificationType type;
    private LocalDateTime notificationDate;
    private NewMemberInvitation newMemberInvitation;
    private NotificationMessage notificationMessage;
    private TaskNotificationMessage taskMessage;
}
