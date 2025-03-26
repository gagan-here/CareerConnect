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
    log.info("Attempting to like the post with id: " + postId);

    Post post =
        postsRepository
            .findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

    boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
    if (alreadyLiked) throw new BadRequestException("Cannot Like the same post again.");

    PostLike postLike = new PostLike();
    postLike.setPostId(postId);
    postLike.setUserId(userId);
    postLikeRepository.save(postLike);
    log.info("Post with id: {} liked sucessfully", postId);

    PostLikedEvent postLikedEvent =
        PostLikedEvent.builder()
            .postId(postId)
            .likedByUserId(userId)
            .creatorId(post.getUserId())
            .build();

    kafkaTemplate.send("post-liked-topic", postId, postLikedEvent);
  }

  @Transactional
  public void unlikePost(Long postId) {
    Long userId = getUserId();

    log.info("Attempting to unlike the post with id: " + postId);

    boolean exists = postsRepository.existsById(postId);
    if (!exists) throw new ResourceNotFoundException("Post not found with id:" + postId);

    boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
    if (!alreadyLiked) throw new BadRequestException("Cannot unLike the post which is not liked.");

    postLikeRepository.deleteByUserIdAndPostId(userId, postId);

    log.info("Post with id: {} unliked sucessfully", postId);
  }

  private static Long getUserId() {
    return UserContextHolder.getCurrentUserId();
  }
}
