package com.daangn.dangunmarket.domain.post.repository.postlike;

import com.daangn.dangunmarket.domain.post.model.PostLike;
import com.daangn.dangunmarket.domain.post.repository.postlike.dto.JoinedWithArea;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PostLikeRepositoryImpl implements PostLikeRepository {

    private PostLikeJpaRepository postLikeJpaRepository;
    private PostLikeJoinRepository postLikeJoinRepository;

    public PostLikeRepositoryImpl(PostLikeJpaRepository postLikeJpaRepository, PostLikeJoinRepository postLikeJoinRepository) {
        this.postLikeJpaRepository = postLikeJpaRepository;
        this.postLikeJoinRepository = postLikeJoinRepository;
    }

    @Override
    public PostLike save(PostLike postLike) {
        return postLikeJpaRepository.save(postLike);
    }

    @Override
    public Slice<JoinedWithArea> findByMemberId(Long memberId, Pageable pageable) {
        return postLikeJoinRepository.findDetailsByMemberId(memberId, pageable);
    }

    @Override
    public Optional<PostLike> findByMemberIdAndPostId(Long memberId, Long postId) {
        return postLikeJpaRepository.findByMemberIdAndPostId(memberId, postId);
    }

    @Override
    public void delete(PostLike postLike) {
        postLikeJpaRepository.delete(postLike);
    }

    @Override
    public boolean existsByMemberIdAndPostId(Long memberId, Long postId) {
        return postLikeJpaRepository.existsByMemberIdAndPostId(memberId, postId);
    }

}
