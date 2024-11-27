package es.roomie.household.kafka;

import es.roomie.household.utils.JsonUtils;
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

    public void sendNewMemberInvitation(NewMemberInvitation newMemberInvitation) {
        log.info("Sending new member confirmation");
        Message<String> message = MessageBuilder
                .withPayload(JsonUtils.toJson(newMemberInvitation))
                .setHeader(TOPIC, "newMember-topic")
                .build();

        kafkaTemplate.send(message);
    }

    public void sendNotification(NotificationMessage notificationMessage) {
        log.info("Sending notification");
        Message<String> message = MessageBuilder
                .withPayload(JsonUtils.toJson(notificationMessage))
                .setHeader(TOPIC, "notification-topic")
                .build();

        kafkaTemplate.send(message);
    }
}
