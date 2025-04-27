package com.careerconnect.postsservice.event;

import lombok.Data;

@Data
public class PostLikedEvent {
  Long postId;
  Long creatorId;
  Long likedByUserId;
}
