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

    Post post = modelMapper.map(postCreateRequestDto, Post.class);
    post.setUserId(userId);

    Post savedPost = postsRepository.save(post);

    PostCreatedEvent postCreatedEvent =
        PostCreatedEvent.builder()
            .postId(savedPost.getId())
            .creatorId(userId)
            .content(savedPost.getContent())
            .build();

    kafkaTemplate.send("post-created-topic", postCreatedEvent);

    return modelMapper.map(savedPost, PostDto.class);
  }

  public PostDto getPostById(Long postId) {
    log.debug("Retrieving post with ID: {}", postId);

    Post post =
        postsRepository
            .findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

    return modelMapper.map(post, PostDto.class);
  }

  public List<PostDto> getAllPostsOfUser(Long userId) {
    List<Post> posts = postsRepository.findByUserId(userId);

    return posts.stream().map((element) -> modelMapper.map(element, PostDto.class)).toList();
  }

  private static Long getCurrentUserId() {
    return UserContextHolder.getCurrentUserId();
  }
}
