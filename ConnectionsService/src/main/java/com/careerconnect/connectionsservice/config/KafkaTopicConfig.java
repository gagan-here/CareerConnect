package com.careerconnect.connectionsservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

  public NewTopic sendConnectionReuestTopic() {
    return new NewTopic("send-connection-request-topic", 3, (short) 1);
  }

  public NewTopic acceptConnectionReuestTopic() {
    return new NewTopic("accept-connection-request-topic", 3, (short) 1);
  }
}
