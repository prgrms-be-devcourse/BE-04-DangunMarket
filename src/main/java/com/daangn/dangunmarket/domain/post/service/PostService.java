package com.daangn.dangunmarket.domain.post.service;

import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.repository.dto.PostDto;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import com.daangn.dangunmarket.domain.post.service.dto.PostCreateRequest;
import com.daangn.dangunmarket.domain.post.service.dto.PostFindResponse;
import com.daangn.dangunmarket.domain.post.service.dto.PostGetResponses;
import com.daangn.dangunmarket.domain.post.service.dto.PostSearchConditionRequest;
import com.daangn.dangunmarket.domain.post.service.dto.PostSearchResponses;
import com.daangn.dangunmarket.domain.post.service.mapper.PostMapper;
import com.daangn.dangunmarket.global.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.daangn.dangunmarket.global.response.ErrorCode.NOT_FOUND_POST_ENTITY;

@Transactional(readOnly = true)
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper mapper;

    public PostService(PostRepository postRepository, PostMapper mapper) {
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Transactional
    public Long createPost(PostCreateRequest request) {
        Post savePost = postRepository.save(mapper.toEntity(request));
        return savePost.getId();
    }


    public PostFindResponse findById(Long productId) {
        Post post = postRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_POST_ENTITY));

        return PostFindResponse.from(post);
    }

    public PostGetResponses getPosts(Long areaId, Pageable pageable) {
        Page<PostDto> postDtoPages = postRepository.getPostsSimple(areaId, pageable);

        PostGetResponses responses = PostGetResponses.from(postDtoPages);
        return responses;
    }

    public PostSearchResponses searchPosts(Long areaId, PostSearchConditionRequest searchCondition) {
        Page<PostDto> postDtoPages = postRepository.getPostsByConditions(areaId, searchCondition);

        PostSearchResponses responses = PostSearchResponses.from(postDtoPages);
        return responses;
    }

}
