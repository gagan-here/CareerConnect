package com.careerconnect.postsservice.controller;

import com.careerconnect.postsservice.dto.PostCreateRequestDto;
import com.careerconnect.postsservice.dto.PostDto;
import com.careerconnect.postsservice.service.PostsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostsController {

  private final PostsService postsService;

  @PostMapping
  public ResponseEntity<PostDto> createPost(
      @RequestBody PostCreateRequestDto postCreateRequestDto,
      HttpServletRequest httpServletRequest) {
    PostDto createdPost = postsService.createPost(postCreateRequestDto, 1L);
    return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
  }
}
