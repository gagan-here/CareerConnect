package com.careerconnect.postsservice.service;

import com.careerconnect.postsservice.entity.PostLike;
import com.careerconnect.postsservice.exception.BadRequestException;
import com.careerconnect.postsservice.exception.ResourceNotFoundException;
import com.careerconnect.postsservice.repository.PostLikeRepository;
import com.careerconnect.postsservice.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {

  private final PostLikeRepository postLikeRepository;
  private final PostsRepository postsRepository;

  public void likePost(Long postId, Long userId) {
    log.info("Attempting to like the post with id: " + postId);

    boolean exists = postsRepository.existsById(postId);
    if (!exists) throw new ResourceNotFoundException("Post not found with id:" + postId);

    boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
    if (alreadyLiked) throw new BadRequestException("Cannot Like the same post again.");

    PostLike postLike = new PostLike();
    postLike.setPostId(postId);
    postLike.setUserId(userId);
    postLikeRepository.save(postLike);
    log.info("Post with id: {} liked sucessfully", postId);
  }
}
