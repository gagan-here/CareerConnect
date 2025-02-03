package com.careerconnect.postsservice.repository;

import com.careerconnect.postsservice.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Post, Long> {
  List<Post> findByUserId(Long userId);
}
