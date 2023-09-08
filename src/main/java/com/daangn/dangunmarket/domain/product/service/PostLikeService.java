package com.daangn.dangunmarket.domain.product.service;

import com.daangn.dangunmarket.domain.product.model.Post;
import com.daangn.dangunmarket.domain.product.model.PostLike;
import com.daangn.dangunmarket.domain.product.repository.post.PostRepository;
import com.daangn.dangunmarket.domain.product.repository.postlike.PostLikeRepository;
import com.daangn.dangunmarket.domain.product.service.dto.PostLikeResponse;
import com.daangn.dangunmarket.global.Exception.EntityNotFoundException;
import com.daangn.dangunmarket.global.Exception.InvalidPostLikeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.daangn.dangunmarket.global.response.ErrorCode.*;

@Service
@Transactional(readOnly = true)
public class PostLikeService {

    private PostLikeRepository postLikeRepository;
    private PostRepository postRepository;

    public PostLikeService(PostLikeRepository postLikeRepository, PostRepository postRepository) {
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public PostLikeResponse likePost(Long memberId, Long postId) {
        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_POST_ENTITY));
        post.like();

        if (postLikeRepository.existsByMemberIdAndPostId(memberId, postId)) {
            throw new InvalidPostLikeException(ALREADY_EXISTS_POST_LIKE);
        }
        PostLike postLike = PostLike.builder()
                .memberId(memberId)
                .post(post)
                .build();
        postLikeRepository.save(postLike);

        return PostLikeResponse.of(post.getLikeCount(), true);
    }

    @Transactional
    public PostLikeResponse cancelLikePost(Long memberId, Long postId) {
        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_POST_ENTITY));
        post.cancelLike();


        PostLike postLike = postLikeRepository
                .findByMemberIdAndPostId(memberId, postId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_POST_LIKE_ENTITY));
        postLikeRepository.delete(postLike);

        return PostLikeResponse.of(post.getLikeCount(), false);
    }

}
