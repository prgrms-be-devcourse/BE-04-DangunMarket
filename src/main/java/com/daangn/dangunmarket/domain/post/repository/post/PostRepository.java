package com.daangn.dangunmarket.domain.post.repository.post;

import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.service.dto.PostGetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Optional<Post> findById(Long productId);

    Page<PostGetResponse> getPostsSimple(Long areaId, Pageable pageable);

}