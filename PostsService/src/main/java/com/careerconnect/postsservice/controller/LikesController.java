package com.careerconnect.postsservice.controller;

import com.careerconnect.postsservice.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikesController {

  private final PostLikeService postLikeService;

  @PostMapping("/{postId}")
  public ResponseEntity<Void> likePost(@PathVariable Long postId) {
    postLikeService.likePost(postId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> unlikePost(@PathVariable Long postId) {
    postLikeService.unlikePost(postId);
    return ResponseEntity.noContent().build();
  }
}
