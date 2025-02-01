package com.careerconnect.postsservice.repository;

import com.careerconnect.postsservice.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
  boolean existsByUserIdAndPostId(Long userId, Long postId);
}
