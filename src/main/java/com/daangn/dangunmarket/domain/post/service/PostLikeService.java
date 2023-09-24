package com.daangn.dangunmarket.domain.post.service;

import com.daangn.dangunmarket.domain.post.exception.InvalidPostLikeException;
import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.model.PostLike;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import com.daangn.dangunmarket.domain.post.repository.postlike.PostLikeRepository;
import com.daangn.dangunmarket.domain.post.repository.postlike.dto.JoinedPostWithArea;
import com.daangn.dangunmarket.domain.post.service.dto.LikedPostFindResponseList;
import com.daangn.dangunmarket.domain.post.service.dto.PostLikeResponse;
import com.daangn.dangunmarket.global.exception.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.daangn.dangunmarket.global.response.ErrorCode.*;

@Service
@Transactional(readOnly = true)
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    public PostLikeService(PostLikeRepository postLikeRepository, PostRepository postRepository) {
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public PostLikeResponse likePost(Long memberId, Long postId) {
        Post post = postRepository
                .findByIdForUpdate(postId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_POST_ENTITY));

        if (postLikeRepository.existsByMemberIdAndPostId(memberId, postId)) {
            throw new InvalidPostLikeException(ALREADY_EXISTS_POST_LIKE);
        }

        post.like();

        if (postLikeRepository.existsByMemberIdAndPostId(memberId, postId)) {
            throw new InvalidPostLikeException(NOT_FOUND_POST_ENTITY);
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

    public LikedPostFindResponseList findByMemberId(Long memberId, Pageable pageable) {
        Slice<JoinedPostWithArea> responseList = postLikeRepository.findByMemberId(memberId, pageable);

        return LikedPostFindResponseList.from(responseList);
    }

}
