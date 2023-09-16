package com.daangn.dangunmarket.domain.post.controller;

import com.daangn.dangunmarket.domain.auth.jwt.CustomUser;
import com.daangn.dangunmarket.domain.post.controller.dto.PostUpdateApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.PostUpdateApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostCreateApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostCreateApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostFindApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostRefreshApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostUpdateStatusApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostUpdateStatusApiResponse;

import com.daangn.dangunmarket.domain.post.controller.dto.post.PostToUpdateApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostGetApiResponses;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostSearchApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostSearchApiResponses;
import com.daangn.dangunmarket.domain.post.controller.mapper.PostApiMapper;
import com.daangn.dangunmarket.domain.post.exception.TooEarlyToRefreshException;
import com.daangn.dangunmarket.domain.post.exception.TooEarlyToRefreshResponse;
import com.daangn.dangunmarket.domain.post.facade.PostFacade;
import com.daangn.dangunmarket.domain.post.facade.dto.PostFindResponseParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostGetResponseParams;
import com.daangn.dangunmarket.domain.post.facade.dto.PostSearchResponseParams;
import com.daangn.dangunmarket.domain.post.facade.dto.PostToUpdateResponseParam;

import com.daangn.dangunmarket.domain.post.service.PostService;

import com.daangn.dangunmarket.domain.post.facade.dto.PostUpdateRequestParam;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/posts",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class PostController {

    private final PostFacade postFacade;
    private final PostService postService;
    private final PostApiMapper mapper;

    public PostController(PostFacade postFacade, PostService postService, PostApiMapper mapper) {
        this.postFacade = postFacade;
        this.postService = postService;
        this.mapper = mapper;
    }

    /**
     * 게시글 등록
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PostCreateApiResponse> createProduct(
            @RequestPart List<MultipartFile> files,
            @RequestPart @Valid PostCreateApiRequest request,
            Authentication authentication
    ) {
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        Long postId = postFacade.createPost(mapper.toPostCreateRequestParam(request, files, customUser.memberId()));
        PostCreateApiResponse response = PostCreateApiResponse.from(postId);

        URI uri = createURI(postId);

        return ResponseEntity.created(uri).body(response);
    }

    /**
     * 게시글 상세 조회
     */
    @GetMapping("/{postId}")
    public ResponseEntity<PostFindApiResponse> findById(@PathVariable Long postId) {
        PostFindResponseParam responseParam = postFacade.findById(postId);
        PostFindApiResponse response = PostFindApiResponse.from(responseParam);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{postId}/trade-status")
    public ResponseEntity<PostUpdateStatusApiResponse> changeStatus(
            @PathVariable Long postId,
            @RequestBody PostUpdateStatusApiRequest request) {

        Long responsePostId = postService.changeStatus(mapper.toPostUpdateStatusRequest(request, postId));
        PostUpdateStatusApiResponse apiResponse = PostUpdateStatusApiResponse.from(responsePostId);

        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/{postId}/refreshed-at")
    public ResponseEntity<PostRefreshApiResponse> refreshPostTime(
            @PathVariable Long postId,
            Authentication authentication
    ) {

        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        PostRefreshApiResponse apiResponse = PostRefreshApiResponse.from(
                postService.refreshTime(postId, customUser.memberId())
        );

        return ResponseEntity.ok(apiResponse);
    }

    /**
     * [Exception] 커스텀 예외 - TooEarlyToRefreshException
     * 해당 예외는 클라이언트에게 유저가 refresh 가능한 남은 시간을 알려주는 응답이다.
     */
    @ExceptionHandler(TooEarlyToRefreshException.class)
    public ResponseEntity<TooEarlyToRefreshResponse> handleTooEarlyToRefreshException(TooEarlyToRefreshException e) {
        TooEarlyToRefreshResponse response = TooEarlyToRefreshResponse.of(
                e.getRemainingDays(),
                e.getRemainingHours(),
                e.getRemainingMinutes());

        return ResponseEntity.ok(response);
    }

    /**
     * 게시글 수정을 위한 조회
     */
    @GetMapping("/{postId}/edit")
    public ResponseEntity<PostToUpdateApiResponse> findPostInfoToUpdate(@PathVariable Long postId, Authentication authentication) {

        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        PostToUpdateResponseParam postInfoToUpdate = postFacade.findPostInfoToUpdateById(customUser.memberId(), postId);

        return ResponseEntity.ok(mapper.toPostToUpdateApiResponse(postInfoToUpdate));
    }

    /**
     * 메인 페이지 내 게시글 리스트 조회
     */
    @GetMapping
    public ResponseEntity<PostGetApiResponses> getPosts(
            Pageable pageable,
            Authentication authentication
    ) {
        CustomUser customUser = (CustomUser) authentication.getPrincipal();

        PostGetResponseParams responseparam = postFacade.getPosts(customUser.memberId(), pageable);
        PostGetApiResponses responses = mapper.toPostGetApiResponses(responseparam);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responses);
    }

    @PutMapping
    public ResponseEntity<PostUpdateApiResponse> updatePost(@RequestBody @Valid PostUpdateApiRequest postUpdateApiRequest) {
        PostUpdateRequestParam postUpdateRequestParam = mapper.toPostUpdateRequestParam(postUpdateApiRequest);
        Long postId = postFacade.updatePost(postUpdateRequestParam);
        PostUpdateApiResponse postUpdateApiResponse = mapper.toPostUpdateApiResponse(postId);

        URI uri = createURI(postId);

        return ResponseEntity.created(uri).body(postUpdateApiResponse);
    }

    /**
     * 게시글 검색
     */
    @GetMapping("/search")
    public ResponseEntity<PostSearchApiResponses> getPostsByConditions(
            @ModelAttribute PostSearchApiRequest postSearchApiRequest,
            Pageable pageable,
            Authentication authentiction
    ) {
        CustomUser customUser = (CustomUser) authentiction.getPrincipal();

        PostSearchResponseParams responseParams = postFacade.getPostsByConditions(
                mapper.toPostSearchRequestParam(postSearchApiRequest, pageable),
                customUser.memberId()
        );
        PostSearchApiResponses responses = mapper.toPostSearchApiResponses(responseParams);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responses);
    }

    private static URI createURI(Long productId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productId)
                .toUri();
    }

}
