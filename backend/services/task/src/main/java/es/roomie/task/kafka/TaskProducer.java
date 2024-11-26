package es.roomie.task.kafka;

import es.roomie.task.utils.JsonUtils;
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
public class TaskProducer {
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendTaskNotification(NotificationMessage notificationMessage) {
        log.info("Sending notification");
        Message<String> message = MessageBuilder
                .withPayload(JsonUtils.toJson(notificationMessage))
                .setHeader(TOPIC, "tasks-topic")
                .build();

        kafkaTemplate.send(message);
    }
}
