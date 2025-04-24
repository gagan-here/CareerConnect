package com.careerconnect.notificationservice.consumer;

import com.careerconnect.connectionsservice.event.AcceptConnectionRequestEvent;
import com.careerconnect.connectionsservice.event.SendConnectionRequestevent;
import com.careerconnect.notificationservice.service.SendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionsServiceConsumer {

  private final SendNotification sendNotification;

  @KafkaListener(topics = "send-connectionrequest-topic")
  public void handleSendConnectionRequest(SendConnectionRequestevent sendConnectionRequestevent) {
    String message =
        "You have received a connection request from user with id: %d"
            + sendConnectionRequestevent.getSenderId();

    sendNotification.send(sendConnectionRequestevent.getReceiverId(), message);
  }

  @KafkaListener(topics = "accept-connectionrequest-topic")
  public void handleAcceptConnectionRequest(
      AcceptConnectionRequestEvent acceptConnectionRequestEvent) {
    String message =
        "Your connection request has been accepted by user with id: %d"
            + acceptConnectionRequestEvent.getReceiverId();

    sendNotification.send(acceptConnectionRequestEvent.getSenderId(), message);
  }
}
