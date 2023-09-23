package com.daangn.dangunmarket.domain.post.repository.post;

import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.repository.dto.PostDto;
import com.daangn.dangunmarket.domain.post.service.dto.PostSearchConditionRequest;
import com.daangn.dangunmarket.global.exception.EntityNotFoundException;
import com.daangn.dangunmarket.global.response.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Optional<Post> findById(Long postId);

    default Post getById(Long productId){
      return findById(productId)
              .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_POST_ENTITY));
    }

    Page<PostDto> getPostsSimple(Long areaId, Pageable pageable);

    Page<PostDto> getPostsByConditions(Long areaId, PostSearchConditionRequest conditions);

    Optional<Post> findByIdForUpdate(@Param(value = "id") Long id);
}
