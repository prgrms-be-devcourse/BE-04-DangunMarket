package com.daangn.dangunmarket.domain.post.repository.post;

import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.repository.dto.PostDto;
import com.daangn.dangunmarket.domain.post.service.dto.PostSearchConditionRequest;
import com.daangn.dangunmarket.global.exception.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static com.daangn.dangunmarket.global.response.ErrorCode.NOT_FOUND_POST_ENTITY;

public interface PostRepository {

    Post save(Post post);

    /**
     *  @throw EntityNotFoundException
     */
    default Post getById(Long productId){
        return findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_POST_ENTITY));
    }
    Optional<Post> findById(Long productId);

    Slice<PostDto> getPostsSimple(Long areaId, Pageable pageable);

    Slice<PostDto> getPostsByConditions(Long areaId, PostSearchConditionRequest conditions);

    Optional<Post> findByIdForUpdate(@Param(value = "id") Long id);
}
