package com.daangn.dangunmarket.domain.post.repository.post;

import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.repository.dto.PostDto;
import com.daangn.dangunmarket.domain.post.service.dto.PostSearchConditionRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository postJpaRepository;
    private final PostQueryRepository postQueryRepository;

    public PostRepositoryImpl(PostJpaRepository postJpaRepository, PostQueryRepository postQueryRepository) {
        this.postJpaRepository = postJpaRepository;
        this.postQueryRepository = postQueryRepository;
    }

    @Override
    public Post save(Post post) {
        return postJpaRepository.save(post);
    }

    @Override
    public Optional<Post> findById(Long productId) {
        return postJpaRepository.findById(productId);
    }

    @Override
    public Slice<PostDto> getPostsSimple(Long areaId, Pageable pageable) {
        return postQueryRepository
                .getPostsSimple(areaId, pageable);
    }

    @Override
    public Slice<PostDto> getPostsByConditions(Long areaId,
                                               PostSearchConditionRequest conditions) {
        return postQueryRepository
                .getPostsByConditions(areaId, conditions);
    }

    @Override
    public Optional<Post> findByIdForUpdate(Long id) {
        return postJpaRepository.findByIdForUpdate(id);
    }

}
