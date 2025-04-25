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

  @KafkaListener(topics = "send-connection-request-topic")
  public void handleSendConnectionRequest(SendConnectionRequestevent sendConnectionRequestevent) {
    log.info("handle connections: handleSendConnectionRequest: {}", sendConnectionRequestevent);
    String message =
        "You have received a connection request from user with id: %d"
            + sendConnectionRequestevent.getSenderId();

    sendNotification.send(sendConnectionRequestevent.getReceiverId(), message);
  }

  @KafkaListener(topics = "accept-connection-request-topic")
  public void handleAcceptConnectionRequest(
      AcceptConnectionRequestEvent acceptConnectionRequestEvent) {
    log.info("handle connections: acceptConnectionRequestEvent: {}", acceptConnectionRequestEvent);
    String message =
        "Your connection request has been accepted by user with id: %d"
            + acceptConnectionRequestEvent.getReceiverId();

    sendNotification.send(acceptConnectionRequestEvent.getSenderId(), message);
  }
}
