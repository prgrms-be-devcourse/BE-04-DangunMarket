package com.daangn.dangunmarket.domain.post.repository;

import com.daangn.dangunmarket.domain.post.model.Post;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private PostJpaRepository postJpaRepository;

    public PostRepositoryImpl(PostJpaRepository postJpaRepository) {
        this.postJpaRepository = postJpaRepository;
    }

    @Override
    public Post save(Post post) {
        return postJpaRepository.save(post);
    }

    @Override
    public Optional<Post> findById(Long productId) {
        return postJpaRepository.findById(productId);
    }

}
