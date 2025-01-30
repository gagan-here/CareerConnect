package com.careerconnect.postsservice.repository;

import com.careerconnect.postsservice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Post, Long> {}
