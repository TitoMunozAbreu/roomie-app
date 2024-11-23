package es.roomie.notification.notification;

import es.roomie.notification.kafka.household.NotificationMessage;
import es.roomie.notification.kafka.household.NewMemberInvitation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

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
}
