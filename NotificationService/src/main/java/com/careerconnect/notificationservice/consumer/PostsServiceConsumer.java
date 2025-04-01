package com.careerconnect.notificationservice.consumer;

import com.careerconnect.postsservice.event.PostCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PostsServiceConsumer {

  @KafkaListener(topics = "post-created-topic")
  public void handlePostCreated(PostCreatedEvent postCreatedEvent) {}
}
