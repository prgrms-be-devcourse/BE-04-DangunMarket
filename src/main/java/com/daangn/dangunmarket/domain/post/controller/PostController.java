package com.daangn.dangunmarket.domain.post.controller;

import com.daangn.dangunmarket.domain.auth.jwt.CustomUser;
import com.daangn.dangunmarket.domain.post.controller.dto.PostCreateApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.PostCreateApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.PostFindApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.PostToUpdateApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.PostGetApiResponses;
import com.daangn.dangunmarket.domain.post.controller.dto.PostSearchApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.PostSearchApiResponses;
import com.daangn.dangunmarket.domain.post.controller.mapper.PostApiMapper;
import com.daangn.dangunmarket.domain.post.facade.PostFacade;
import com.daangn.dangunmarket.domain.post.facade.dto.PostFindResponseParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostGetResponseParams;
import com.daangn.dangunmarket.domain.post.facade.dto.PostSearchResponseParams;
import com.daangn.dangunmarket.domain.post.facade.dto.PostToUpdateResponseParam;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/posts",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class PostController {

    private final PostFacade postFacade;
    private final PostApiMapper mapper;

    public PostController(PostFacade postFacade, PostApiMapper mapper ) {
        this.postFacade = postFacade;
        this.mapper = mapper;
    }

    /**
     * 게시글 등록
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostCreateApiResponse> createProduct(
            @RequestBody @Valid PostCreateApiRequest request,
            Authentication authentication
    ) {
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        Long productId = postFacade.createPost(mapper.toPostCreateRequestParam(request, customUser.memberId()));
        PostCreateApiResponse response = PostCreateApiResponse.from(productId);

        URI uri = createURI(productId);

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
