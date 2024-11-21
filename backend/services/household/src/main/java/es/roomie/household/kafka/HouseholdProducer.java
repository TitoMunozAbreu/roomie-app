package es.roomie.household.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@AllArgsConstructor
@Slf4j
public class HouseholdProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendNewMemberConfirmation(NotificationMessage notificationMessage) {
        log.info("Sending new member confirmation");
        Message<NotificationMessage> message = MessageBuilder
                .withPayload(notificationMessage)
                .setHeader(TOPIC, "newMember-topic")
                .build();

        kafkaTemplate.send(message);
    }

    public void sendNotification(NotificationMessage notificationMessage) {
        log.info("Sending notification");
        Message<NotificationMessage> message = MessageBuilder
                .withPayload(notificationMessage)
                .setHeader(TOPIC, "notification-topic")
                .build();

        kafkaTemplate.send(message);
    }
}
