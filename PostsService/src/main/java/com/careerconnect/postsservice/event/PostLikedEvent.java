package com.careerconnect.postsservice.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostLikedEvent {
  Long postId;
  Long creatorId;
  Long likedByUserId;
}
