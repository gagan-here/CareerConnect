package com.careerconnect.connectionsservice.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendConnectionRequestevent {
  private Long senderId;
  private Long receiverId;
}
