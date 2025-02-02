package es.roomie.notification.email;

import lombok.Getter;

/**
 * Enum representing various email templates used in the application.
 * Each enum constant corresponds to a specific email template with its associated subject.
 */
@Getter
public enum EmailTemplates {
    NEW_MEMBER_CONFIRMATION("new-member-invitation.html", "Welcome to the household"),
    HOUSEHOLD_CONFIRMATION("household-notification.html", "Message from household"),
    TASK_CONFIRMATION("task-notification.html", "Message from household");

    private final String template;
    private final String subject;

    EmailTemplates(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }
}
