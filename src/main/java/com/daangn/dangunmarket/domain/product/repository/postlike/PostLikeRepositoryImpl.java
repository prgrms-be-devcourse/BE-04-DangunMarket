package com.daangn.dangunmarket.domain.product.repository.postlike;

import com.daangn.dangunmarket.domain.product.model.PostLike;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PostLikeRepositoryImpl implements PostLikeRepository {

    private PostLikeJpaRepository postLikeJpaRepository;

    public PostLikeRepositoryImpl(PostLikeJpaRepository postLikeJpaRepository) {
        this.postLikeJpaRepository = postLikeJpaRepository;
    }

    @Override
    public PostLike save(PostLike postLike) {
        return postLikeJpaRepository.save(postLike);
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
