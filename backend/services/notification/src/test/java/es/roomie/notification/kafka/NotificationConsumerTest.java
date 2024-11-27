package es.roomie.notification.kafka;

import es.roomie.notification.email.EmailService;
import es.roomie.notification.notification.Notification;
import es.roomie.notification.notification.NotificationRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static es.roomie.notification.notification.NotificationType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationConsumer notificationConsumer;

    private static final String VALID_NEW_MEMBER_MESSAGE = "{\"memberEmail\":\"test@example.com\",\"title\":\"Welcome!\",\"description\":\"You are invited to join.\",\"urlConfirmInvitation\":\"http://example.com/confirm\"}";
    private static final String VALID_NOTIFICATION_MESSAGE = "{\"memberEmail\":\"test@example.com\",\"title\":\"Notification Title\",\"description\":\"Notification Description\"}";
    private static final String VALID_TASK_MESSAGE = "{\"memberEmail\":\"test@example.com\",\"title\":\"Task Title\",\"description\":\"Task Description\",\"taskName\":\"Homework\",\"taskDueDate\":\"2023-12-31\"}";

    @BeforeEach
    void setUp() {
        // Any necessary setup can be done here.
    }

    @Test
    void testConsumeNewMemberInvitation() throws MessagingException {
        notificationConsumer.consumeNewMemberInvitation(VALID_NEW_MEMBER_MESSAGE);

        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository).save(notificationCaptor.capture());
        verify(emailService).sendNewMemberInvitationEmail(eq("test@example.com"), eq("Welcome!"), eq("You are invited to join."), eq("http://example.com/confirm"));

        Notification notification = notificationCaptor.getValue();
        assertEquals(NEW_MEMBER_CONFIRMATION, notification.getType());
        assertNotNull(notification.getNotificationDate());
        assertNotNull(notification.getNewMemberInvitation());
    }

    @Test
    void testConsumeNotification() throws MessagingException {
        notificationConsumer.consumeNotification(VALID_NOTIFICATION_MESSAGE);

        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository).save(notificationCaptor.capture());
        verify(emailService).sendNotificationEmail(eq("test@example.com"), eq("Notification Title"), eq("Notification Description"));

        Notification notification = notificationCaptor.getValue();
        assertEquals(HOUSEHOLD_CONFIRMATION, notification.getType());
        assertNotNull(notification.getNotificationDate());
        assertNotNull(notification.getNotificationMessage());
    }

    @Test
    void testConsumeTasksNotification() throws MessagingException {
        notificationConsumer.consumeTasksNotification(VALID_TASK_MESSAGE);

        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository).save(notificationCaptor.capture());
        verify(emailService).sendTaskNotificationEmail(eq("test@example.com"), eq("Task Title"), eq("Task Description"), eq("Homework"), eq("2023-12-31"));

        Notification notification = notificationCaptor.getValue();
        assertEquals(TASK_CONFIRMATION, notification.getType());
        assertNotNull(notification.getNotificationDate());
        assertNotNull(notification.getTaskMessage());
    }

    @Test
    void testConsumeNewMemberInvitationThrowsMessagingException() throws MessagingException {
        doThrow(new MessagingException("Email sending failure")).when(emailService).sendNewMemberInvitationEmail(any(), any(), any(), any());

        assertThrows(MessagingException.class, () -> notificationConsumer.consumeNewMemberInvitation(VALID_NEW_MEMBER_MESSAGE));
    }

    @Test
    void testConsumeNotificationThrowsMessagingException() throws MessagingException {
        doThrow(new MessagingException("Email sending failure")).when(emailService).sendNotificationEmail(any(), any(), any());

        assertThrows(MessagingException.class, () -> notificationConsumer.consumeNotification(VALID_NOTIFICATION_MESSAGE));
    }

    @Test
    void testConsumeTasksNotificationThrowsMessagingException() throws MessagingException {
        doThrow(new MessagingException("Email sending failure")).when(emailService).sendTaskNotificationEmail(any(), any(), any(), any(), any());

        assertThrows(MessagingException.class, () -> notificationConsumer.consumeTasksNotification(VALID_TASK_MESSAGE));
    }

    @Test
    void testConsumeNewMemberInvitationWithInvalidJson() {
        String invalidMessage = "{\"invalidField\":\"value\"}";

        assertThrows(Exception.class, () -> notificationConsumer.consumeNewMemberInvitation(invalidMessage));
    }

    @Test
    void testConsumeNotificationWithInvalidJson() {
        String invalidMessage = "{\"invalidField\":\"value\"}";

        assertThrows(Exception.class, () -> notificationConsumer.consumeNotification(invalidMessage));
    }

    @Test
    void testConsumeTasksNotificationWithInvalidJson() {
        String invalidMessage = "{\"invalidField\":\"value\"}";

        assertThrows(Exception.class, () -> notificationConsumer.consumeTasksNotification(invalidMessage));
    }
}