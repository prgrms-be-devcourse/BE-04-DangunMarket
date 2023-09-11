package com.daangn.dangunmarket.domain.post.repository.postlike;

import com.daangn.dangunmarket.domain.post.model.PostLike;
import com.daangn.dangunmarket.domain.post.repository.postlike.dto.JoinedWithArea;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository {
    PostLike save(PostLike postLike);

    Slice<JoinedWithArea> findByMemberId(Long memberId, Pageable pageable);

    Optional<PostLike> findByMemberIdAndPostId(Long memberId, Long postId);

    void delete(PostLike postLike);

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);
}
