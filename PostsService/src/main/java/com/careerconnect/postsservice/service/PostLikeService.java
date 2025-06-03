package com.careerconnect.postsservice.service;

import com.careerconnect.postsservice.auth.UserContextHolder;
import com.careerconnect.postsservice.entity.Post;
import com.careerconnect.postsservice.entity.PostLike;
import com.careerconnect.postsservice.event.PostLikedEvent;
import com.careerconnect.postsservice.exception.BadRequestException;
import com.careerconnect.postsservice.exception.ResourceNotFoundException;
import com.careerconnect.postsservice.repository.PostLikeRepository;
import com.careerconnect.postsservice.repository.PostsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {

  private final PostLikeRepository postLikeRepository;
  private final PostsRepository postsRepository;
  private final KafkaTemplate<Long, PostLikedEvent> kafkaTemplate;

  public void likePost(Long postId) {
    Long userId = getUserId();
    log.info("User {} attempting to like post {}", userId, postId);

    Post post =
        postsRepository
            .findById(postId)
            .orElseThrow(
                () -> {
                  log.error("Post not found during like attempt. Post ID: {}", postId);
                  return new ResourceNotFoundException("Post not found with id: " + postId);
                });

    boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
    if (alreadyLiked) {
      log.warn("User {} attempted to like post {} again", userId, postId);
      throw new BadRequestException("Cannot Like the same post again.");
    }

    PostLike postLike = new PostLike();
    postLike.setPostId(postId);
    postLike.setUserId(userId);
    postLikeRepository.save(postLike);
    log.info("User {} successfully liked post {}", userId, postId);

    PostLikedEvent postLikedEvent =
        PostLikedEvent.builder()
            .postId(postId)
            .likedByUserId(userId)
            .creatorId(post.getUserId())
            .build();

    try {
      kafkaTemplate.send("post-liked-topic", postId, postLikedEvent);
      log.debug("PostLikedEvent published for post {} by user {}", postId, userId);
    } catch (Exception e) {
      log.error("Failed to publish PostLikedEvent for post {} by user {}", postId, userId, e);
    }
  }

  @Transactional
  public void unlikePost(Long postId) {
    Long userId = getUserId();
    log.info("User {} attempting to unlike post {}", userId, postId);

    if (!postsRepository.existsById(postId)) {
      log.error("Post {} not found during unlike attempt.", postId);
      throw new ResourceNotFoundException("Post not found with id:" + postId);
    }

    boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
    if (!alreadyLiked) {
      log.warn("User {} attempted to unlike post {} which wasn't liked", userId, postId);
      throw new BadRequestException("Cannot unLike the post which is not liked.");
    }

    postLikeRepository.deleteByUserIdAndPostId(userId, postId);
    log.info("User {} successfully unliked post {}", userId, postId);
  }

  private static Long getUserId() {
    return UserContextHolder.getCurrentUserId();
  }
}
