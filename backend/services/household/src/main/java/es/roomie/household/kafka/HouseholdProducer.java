package es.roomie.household.kafka;

import es.roomie.household.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

/**
 * HouseholdProducer is a service that handles the production of messages
 * related to household events in a Kafka messaging system.
 * It sends new member invitations and notification messages to specified Kafka topics.
 */
@Service
@AllArgsConstructor
@Slf4j
public class HouseholdProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Sends a new member invitation message to the 'newMember-topic'.
     *
     * @param newMemberInvitation the invitation details to be sent
     */
    public void sendNewMemberInvitation(NewMemberInvitation newMemberInvitation) {
        log.info("Sending new member confirmation");
        Message<String> message = MessageBuilder
                .withPayload(JsonUtils.toJson(newMemberInvitation))
                .setHeader(TOPIC, "newMember-topic")
                .build();

        kafkaTemplate.send(message);
    }

    /**
     * Sends a notification message to the 'notification-topic'.
     *
     * @param notificationMessage the notification details to be sent
     */
    public void sendNotification(NotificationMessage notificationMessage) {
        log.info("Sending notification");
        Message<String> message = MessageBuilder
                .withPayload(JsonUtils.toJson(notificationMessage))
                .setHeader(TOPIC, "notification-topic")
                .build();

        kafkaTemplate.send(message);
    }
}
