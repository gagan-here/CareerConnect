package com.careerconnect.notificationservice.consumer;

import com.careerconnect.notificationservice.clients.ConnectionsClient;
import com.careerconnect.notificationservice.dto.PersonDto;
import com.careerconnect.notificationservice.entity.Notification;
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
    List<PersonDto> connections =
        connectionsClient.getFirstConnections(postCreatedEvent.getCreatorId());

    for (PersonDto connection : connections) {
      sendNotification(
          connection.getUserId(),
          "Your connection "
              + postCreatedEvent.getCreatorId()
              + "has created "
              + "a post, Check it out");
    }
  }

  public void sendNotification(Long userId, String message) {
    Notification notification = new Notification();
    notification.setMessage(message);
    notification.setUserId(userId);
  }
}
