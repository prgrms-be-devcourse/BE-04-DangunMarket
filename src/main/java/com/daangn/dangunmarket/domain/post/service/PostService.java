package com.daangn.dangunmarket.domain.post.service;

import com.daangn.dangunmarket.domain.post.exception.UnauthorizedAccessException;
import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.repository.dto.PostDto;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import com.daangn.dangunmarket.domain.post.service.dto.PostCreateRequest;
import com.daangn.dangunmarket.domain.post.service.dto.PostFindResponse;
import com.daangn.dangunmarket.domain.post.service.dto.PostGetResponses;
import com.daangn.dangunmarket.domain.post.service.dto.PostSearchConditionRequest;
import com.daangn.dangunmarket.domain.post.service.dto.PostSearchResponses;
import com.daangn.dangunmarket.domain.post.service.dto.PostToUpdateResponse;
import com.daangn.dangunmarket.domain.post.service.dto.PostUpdateRequest;
import com.daangn.dangunmarket.domain.post.service.dto.PostUpdateStatusRequest;
import com.daangn.dangunmarket.domain.post.service.mapper.PostDtoMapper;
import com.daangn.dangunmarket.domain.post.service.mapper.PostMapper;
import com.daangn.dangunmarket.global.TimeGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.daangn.dangunmarket.global.response.ErrorCode.POST_NOT_CREATED_BY_USER;

@Transactional(readOnly = true)
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostDtoMapper postDtoMapper;
    private final PostMapper mapper;
    private final TimeGenerator timeGenerator;

    public PostService(PostRepository postRepository, PostDtoMapper postDtoMapper, PostMapper mapper, TimeGenerator timeGenerator) {
        this.postRepository = postRepository;
        this.postDtoMapper = postDtoMapper;
        this.mapper = mapper;
        this.timeGenerator = timeGenerator;
    }

    @Transactional
    public Long createPost(PostCreateRequest request) {
        Post savePost = postRepository.save(mapper.toEntity(request));
        return savePost.getId();
    }

    public PostFindResponse findById(Long postId) {
        Post post = postRepository.getById(postId);

        return PostFindResponse.from(post);
    }

    @Transactional
    public Long changeStatus(PostUpdateStatusRequest request) {
        Post post = postRepository.getById(request.postId());

        post.changeTradeStatus(request.tradeStatus());

        return post.getId();
    }

    @Transactional
    public Long refreshTime(Long postId, Long memberId) {
        Post post = postRepository.getById(postId);

        if (post.isNotOwner(memberId)) {
            throw new IllegalStateException("해당 유저는 게시글의 주인이 아닙니다.");
        }

        post.changeRefreshedAt(timeGenerator.getCurrentTime());

        return post.getId();
    }

    public PostToUpdateResponse getPostInfoToUpdate(Long memberId, Long postId) {
        Post post = postRepository.getById(postId);

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
        Post postForUpdate = postRepository.getById(request.postId());

        postForUpdate.edit(request.toPostEditor());

        return postForUpdate.getId();
    }

    public PostSearchResponses searchPosts(Long areaId, PostSearchConditionRequest searchCondition) {
        Page<PostDto> postDtoPages = postRepository.getPostsByConditions(areaId, searchCondition);

        return PostSearchResponses.from(postDtoPages);
    }

    @Transactional
    public void deletePost(Long memberId, Long postId) {
        Post post = postRepository.getById(postId);

        post.deleteByMemberId(memberId);
    }

}
