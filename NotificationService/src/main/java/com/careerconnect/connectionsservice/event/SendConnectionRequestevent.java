package com.careerconnect.connectionsservice.event;

import lombok.Data;

@Data
public class SendConnectionRequestevent {
  private Long senderId;
  private Long receiverId;
}
