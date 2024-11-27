package es.roomie.task.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Configuration class for Kafka topics.
 * This class defines the Kafka topic that will be used in the application.
 */
@Configuration
public class KafkaTopicConfig {

    /**
     * Creates a new Kafka topic named "tasks-topic".
     * @return a NewTopic object representing the configured topic
     */
    @Bean
    public NewTopic topicConfig() {
        return TopicBuilder
                .name("tasks-topic")
                .build();
    }
}
