package es.roomie.household.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Configuration class for Kafka topics.
 * This class defines the necessary topics for the Kafka messaging system.
 */
@Configuration
public class KafkaTopicConfig {

    /**
     * Creates a new Kafka topic for new members.
     * @return a NewTopic instance representing the "newMember-topic".
     */
    @Bean
    public NewTopic newMemberTopic() {
        return TopicBuilder
                .name("newMember-topic")
                .build();
    }

    /**
     * Creates a new Kafka topic for notifications.
     * @return a NewTopic instance representing the "notification-topic".
     */
    @Bean
    public NewTopic notificationTopic() {
        return TopicBuilder
                .name("notification-topic")
                .build();
    }
}
