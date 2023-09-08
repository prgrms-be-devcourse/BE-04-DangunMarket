package com.daangn.dangunmarket.domain.product.repository.postlike;

import com.daangn.dangunmarket.domain.product.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeJpaRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByMemberIdAndPostId(Long memberId, Long postId);

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);
}
