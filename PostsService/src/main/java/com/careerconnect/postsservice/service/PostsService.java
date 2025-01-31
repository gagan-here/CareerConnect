package com.careerconnect.postsservice.service;

import com.careerconnect.postsservice.dto.PostCreateRequestDto;
import com.careerconnect.postsservice.dto.PostDto;
import com.careerconnect.postsservice.entity.Post;
import com.careerconnect.postsservice.exception.ResourceNotFoundException;
import com.careerconnect.postsservice.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostsService {

  private final PostsRepository postsRepository;
  private final ModelMapper modelMapper;

  public PostDto createPost(PostCreateRequestDto postCreateRequestDto, Long userId) {
    Post post = modelMapper.map(postCreateRequestDto, Post.class);
    post.setUserId(userId);

    Post savedPost = postsRepository.save(post);
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
}
