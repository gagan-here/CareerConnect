package com.careerconnect.postsservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

  @Bean
  public NewTopic postCreatedTopic() {
    return new NewTopic("post-created-topic", 3, (short) 1);
  }
}
