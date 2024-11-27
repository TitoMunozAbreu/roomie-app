package es.roomie.household.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic newMemberTopic() {
        return TopicBuilder
                .name("newMember-topic")
                .build();
    }

    @Bean
    public NewTopic notificationTopic() {
        return TopicBuilder
                .name("notification-topic")
                .build();
    }
}
