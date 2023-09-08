package com.daangn.dangunmarket.domain.post.repository;

import com.daangn.dangunmarket.domain.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<Post, Long> {
}
