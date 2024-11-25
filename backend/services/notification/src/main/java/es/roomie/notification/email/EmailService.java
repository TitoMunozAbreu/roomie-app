package es.roomie.notification.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;

import static es.roomie.notification.email.EmailTemplates.HOUSEHOLD_CONFIRMATION;
import static es.roomie.notification.email.EmailTemplates.NEW_MEMBER_CONFIRMATION;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendNewMemberInvitationEmail(String destinationEmail,
                                             String emailTitle,
                                             String emailDescription,
                                             String urlConfirmInvitation) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED, UTF_8.name());

        messageHelper.setFrom("roomie@mail.com");
        messageHelper.setSubject(NEW_MEMBER_CONFIRMATION.getSubject());

        String templateName = NEW_MEMBER_CONFIRMATION.getTemplate();

        HashMap<String, Object> variables = new HashMap<>();
        variables.put("title", emailTitle);
        variables.put("description", emailDescription);
        variables.put("urlConfirmInvatation", urlConfirmInvitation);

        Context context = new Context();
        context.setVariables(variables);

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);

            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info("INFO - Email successfully sent to '{}' with template {}", destinationEmail, templateName);
        } catch (MessagingException e) {
            log.warn("WARNING - Cannot send Email to {} ", destinationEmail);
        }
    }

    @Async
    public void sendNotificationEmail(String destinationEmail,
                                      String emailTitle,
                                      String emailDescription) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED, UTF_8.name());

        messageHelper.setFrom("roomie@mail.com");
        messageHelper.setSubject(HOUSEHOLD_CONFIRMATION.getSubject());

        String templateName = HOUSEHOLD_CONFIRMATION.getTemplate();

        HashMap<String, Object> variables = new HashMap<>();
        variables.put("title", emailTitle);
        variables.put("description", emailDescription);

        Context context = new Context();
        context.setVariables(variables);

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);

            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info("INFO - Email successfully sent to '{}' with template {}", destinationEmail, templateName);
        } catch (MessagingException e) {
            log.warn("WARNING - Cannot send Email to {} ", destinationEmail);
        }
    }

}
