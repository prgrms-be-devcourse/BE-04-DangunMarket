package com.daangn.dangunmarket.domain.post.controller;

import com.daangn.dangunmarket.domain.auth.jwt.CustomUser;
import com.daangn.dangunmarket.domain.post.controller.dto.*;
import com.daangn.dangunmarket.domain.post.controller.mapper.PostApiMapper;
import com.daangn.dangunmarket.domain.post.facade.PostFacade;
import com.daangn.dangunmarket.domain.post.facade.dto.PostFindResponseParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostToUpdateResponseParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostUpdateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostsGetResponseParam;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/{postId}")
    public ResponseEntity<PostFindApiResponse> findById(@PathVariable Long postId) {
        PostFindResponseParam responseParam = postFacade.findById(postId);
        PostFindApiResponse response = PostFindApiResponse.from(responseParam);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}/edit")
    public ResponseEntity<PostToUpdateApiResponse> findPostInfoToUpdate(@PathVariable Long postId, Authentication authentication) {

        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        PostToUpdateResponseParam postInfoToUpdate = postFacade.findPostInfoToUpdateById(customUser.memberId(), postId);

        return ResponseEntity.ok(mapper.toPostToUpdateApiResponse(postInfoToUpdate));
    }

    @GetMapping
    public ResponseEntity<PostGetApiResponses> getPosts(
            Pageable pageable,
            Authentication authentication
    ) {
        CustomUser customUser = (CustomUser) authentication.getPrincipal();

        PostsGetResponseParam responseparam = postFacade.getPosts(customUser.memberId(), pageable);
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

    private static URI createURI(Long productId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productId)
                .toUri();
    }

}
