package com.daangn.dangunmarket.domain.post.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.daangn.dangunmarket.domain.post.exception.UnauthorizedAccessException;
import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.repository.dto.PostDto;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import com.daangn.dangunmarket.domain.post.service.dto.*;
import com.daangn.dangunmarket.domain.post.service.mapper.PostDtoMapper;
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
import static com.daangn.dangunmarket.global.response.ErrorCode.POST_NOT_CREATED_BY_USER;

@Transactional(readOnly = true)
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostDtoMapper postDtoMapper;
    private final PostMapper mapper;

    public PostService(PostRepository postRepository, PostDtoMapper postDtoMapper, PostMapper mapper) {
        this.postRepository = postRepository;
        this.postDtoMapper = postDtoMapper;
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

    public PostToUpdateResponse getPostInfoToUpdate(Long memberId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_POST_ENTITY));

        if (!post.isCreatedBy(memberId)) {
            throw new UnauthorizedAccessException(POST_NOT_CREATED_BY_USER);
        }

        return postDtoMapper.toPostToUpdateResponse(post);
    }

    public PostGetResponses getPosts(Long areaId, Pageable pageable) {
        Page<PostDto> postDtoPages = postRepository.getPostsSimple(areaId, pageable);

        return PostGetResponses.from(postDtoPages);
    }

    @Transactional
    public Long updatePost(PostUpdateRequest request) {
        Post postForUpdate = postRepository.findById(request.postId())
                .orElseThrow(() -> new NotFoundException("해당 게시글이 존재하지 않습니다."));

        postForUpdate.edit(request.toPostEditor());

        return postForUpdate.getId();
    }

    public PostSearchResponses searchPosts(Long areaId, PostSearchConditionRequest searchCondition) {
        Page<PostDto> postDtoPages = postRepository.getPostsByConditions(areaId, searchCondition);

        return PostSearchResponses.from(postDtoPages);
    }

}
