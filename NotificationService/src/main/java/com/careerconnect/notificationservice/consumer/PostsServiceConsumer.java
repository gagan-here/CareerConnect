package com.careerconnect.notificationservice.consumer;

import com.careerconnect.notificationservice.clients.ConnectionsClient;
import com.careerconnect.notificationservice.dto.PersonDto;
import com.careerconnect.notificationservice.entity.Notification;
import com.careerconnect.notificationservice.repository.NotificationRepository;
import com.careerconnect.postsservice.event.PostCreatedEvent;
import com.careerconnect.postsservice.event.PostLikedEvent;
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
  private final NotificationRepository notificationRepository;

  @KafkaListener(topics = "post-created-topic")
  public void handlePostCreated(PostCreatedEvent postCreatedEvent) {
    log.info("Sending notifications: handlePostCreated: {}", postCreatedEvent);
    List<PersonDto> connections =
        connectionsClient.getFirstConnections(postCreatedEvent.getCreatorId());

    for (PersonDto connection : connections) {
      sendNotification(
          connection.getUserId(),
          "Your connection "
              + postCreatedEvent.getCreatorId()
              + "has recently created "
              + "a post, Do have a look!!");
    }
  }

  @KafkaListener(topics = "post-liked-topic")
  public void handlePostLiked(PostLikedEvent postLikedEvent) {
    log.info("Sending notifications: handlePostLiked: {}", postLikedEvent);
    String message =
        String.format(
            "Your post, %d has been liked by %d",
            postLikedEvent.getPostId(), postLikedEvent.getLikedByUserId());

    sendNotification(postLikedEvent.getCreatorId(), message);
  }

  public void sendNotification(Long userId, String message) {
    Notification notification = new Notification();
    notification.setMessage(message);
    notification.setUserId(userId);

    notificationRepository.save(notification);
  }
}
