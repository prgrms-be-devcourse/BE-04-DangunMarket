package com.daangn.dangunmarket.domain.post.service;

import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import com.daangn.dangunmarket.domain.post.service.dto.PostCreateRequest;
import com.daangn.dangunmarket.domain.post.service.mapper.PostMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class PostService {

    private PostRepository postRepository;
    private PostMapper mapper;

    public PostService(PostRepository postRepository, PostMapper mapper) {
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Transactional
    public Long createPost(PostCreateRequest request) {
        Post savePost = postRepository.save(mapper.toEntity(request));
        return savePost.getId();
    }

}
