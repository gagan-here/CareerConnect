package com.careerconnect.postsservice.service;

import com.careerconnect.postsservice.auth.UserContextHolder;
import com.careerconnect.postsservice.clients.ConnectionsClient;
import com.careerconnect.postsservice.dto.PostCreateRequestDto;
import com.careerconnect.postsservice.dto.PostDto;
import com.careerconnect.postsservice.entity.Post;
import com.careerconnect.postsservice.event.PostCreatedEvent;
import com.careerconnect.postsservice.exception.ResourceNotFoundException;
import com.careerconnect.postsservice.repository.PostsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostsService {

  private final PostsRepository postsRepository;
  private final ModelMapper modelMapper;
  private final ConnectionsClient connectionsClient;

  private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;

  public PostDto createPost(PostCreateRequestDto postCreateRequestDto) {
    Long userId = getCurrentUserId();
    log.info("Creating post for user: {}", userId);

    Post post = modelMapper.map(postCreateRequestDto, Post.class);
    post.setUserId(userId);

    Post savedPost = postsRepository.save(post);
    log.info("Post created successfully. ID: {}, User: {}", savedPost.getId(), userId);

    PostCreatedEvent postCreatedEvent =
        PostCreatedEvent.builder()
            .postId(savedPost.getId())
            .creatorId(userId)
            .content(savedPost.getContent())
            .build();

    try {
      kafkaTemplate.send("post-created-topic", postCreatedEvent);
      log.debug("PostCreatedEvent published for post ID: {}", savedPost.getId());
    } catch (Exception e) {
      log.error("Failed to publish PostCreatedEvent for post ID: {}", savedPost.getId(), e);
    }

    return modelMapper.map(savedPost, PostDto.class);
  }

  public PostDto getPostById(Long postId) {
    log.debug("Fetching post with ID: {}", postId);

    Post post =
        postsRepository
            .findById(postId)
            .orElseThrow(
                () -> {
                  log.error("Post not found with ID: {}", postId);
                  return new ResourceNotFoundException("Post not found with id: " + postId);
                });

    log.debug("Successfully retrieved post ID: {}", postId);
    return modelMapper.map(post, PostDto.class);
  }

  public List<PostDto> getAllPostsOfUser(Long userId) {
    log.debug("Fetching all posts for user: {}", userId);

    List<Post> posts = postsRepository.findByUserId(userId);
    log.info("Found {} posts for user: {}", posts.size(), userId);

    return posts.stream().map(post -> modelMapper.map(post, PostDto.class)).toList();
  }

  private static Long getCurrentUserId() {
    return UserContextHolder.getCurrentUserId();
  }
}
