package com.careerconnect.notificationservice.consumer;

import com.careerconnect.notificationservice.clients.ConnectionsClient;
import com.careerconnect.notificationservice.dto.PersonDto;
import com.careerconnect.postsservice.event.PostCreatedEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsServiceConsumer {

  private final ConnectionsClient connectionsClient;

  @KafkaListener(topics = "post-created-topic")
  public void handlePostCreated(PostCreatedEvent postCreatedEvent) {
    List<PersonDto> firstConnections =
        connectionsClient.getFirstConnections(postCreatedEvent.getCreatorId());
  }
}
