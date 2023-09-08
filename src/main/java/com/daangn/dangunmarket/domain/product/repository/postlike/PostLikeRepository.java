package com.daangn.dangunmarket.domain.product.repository.postlike;

import com.daangn.dangunmarket.domain.product.model.PostLike;

import java.util.Optional;

public interface PostLikeRepository {
    PostLike save(PostLike postLike);

    Optional<PostLike> findByMemberIdAndPostId(Long memberId, Long postId);

    void delete(PostLike postLike);

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);
}
