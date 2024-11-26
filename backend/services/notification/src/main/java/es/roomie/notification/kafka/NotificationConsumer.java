package es.roomie.notification.kafka;

import es.roomie.notification.email.EmailService;
import es.roomie.notification.kafka.household.NewMemberInvitation;
import es.roomie.notification.kafka.household.NotificationMessage;
import es.roomie.notification.kafka.task.TaskNotificationMessage;
import es.roomie.notification.notification.Notification;
import es.roomie.notification.notification.NotificationRepository;
import es.roomie.notification.utils.JsonUtils;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static es.roomie.notification.notification.NotificationType.*;
import static java.time.LocalDateTime.now;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationConsumer {

    private NotificationRepository notificationRepository;
    private EmailService emailService;

    @KafkaListener(topics = "newMember-topic", groupId = "newMemberGroup")
    public void consumeNewMemberInvitation(String message) throws MessagingException {
        log.info("Consuming newMemberInvitation from newMember-topic:: {}", message);
        NewMemberInvitation newMemberInvitation = JsonUtils.fromJson(message, NewMemberInvitation.class);

        notificationRepository.save(Notification.builder()
                .type(NEW_MEMBER_CONFIRMATION)
                .notificationDate(now())
                .newMemberInvitation(newMemberInvitation)
                .build());

        emailService.sendNewMemberInvitationEmail(
                newMemberInvitation.memberEmail(),
                newMemberInvitation.title(),
                newMemberInvitation.description(),
                newMemberInvitation.urlConfirmInvitation());
    }

    @KafkaListener(topics = "notification-topic", groupId = "notificationGroup")
    public void consumeNotification(String message) throws MessagingException {
        log.info("Consuming notification from notification-topic:: {}", message);
        NotificationMessage notificationMessage = JsonUtils.fromJson(message, NotificationMessage.class);

        notificationRepository.save(Notification.builder()
                .type(HOUSEHOLD_CONFIRMATION)
                .notificationDate(now())
                .notificationMessage(notificationMessage)
                .build());

        emailService.sendNotificationEmail(
                notificationMessage.memberEmail(),
                notificationMessage.title(),
                notificationMessage.description()
        );

    }

    @KafkaListener(topics = "tasks-topic", groupId = "taskGroup")
    public void consumeTasksNotification(String message) throws MessagingException {
        log.info("Consuming tasks notification from task-topic:: {}", message);
        TaskNotificationMessage taskMessage = JsonUtils.fromJson(message, TaskNotificationMessage.class);

        notificationRepository.save(Notification.builder()
                .type(TASK_CONFIRMATION)
                .notificationDate(now())
                .taskMessage(taskMessage)
                .build());

        emailService.sendTaskNotificationEmail(
                taskMessage.memberEmail(),
                taskMessage.title(),
                taskMessage.description(),
                taskMessage.taskName(),
                taskMessage.taskDueDate()
        );

    }
}
