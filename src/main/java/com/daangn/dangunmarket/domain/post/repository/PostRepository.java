package com.daangn.dangunmarket.domain.post.repository;

import com.daangn.dangunmarket.domain.post.model.Post;

import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Optional<Post> findById(Long productId);
}
