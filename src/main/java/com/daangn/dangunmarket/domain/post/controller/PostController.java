package com.daangn.dangunmarket.domain.post.controller;

import com.daangn.dangunmarket.domain.auth.jwt.CustomUser;
import com.daangn.dangunmarket.domain.post.controller.dto.PostCreateApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.PostCreateApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.PostFindApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.PostToUpdateApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.PostGetApiResponses;
import com.daangn.dangunmarket.domain.post.controller.mapper.PostApiMapper;
import com.daangn.dangunmarket.domain.post.controller.mapper.PostUpdateApiMapper;
import com.daangn.dangunmarket.domain.post.facade.PostFacade;
import com.daangn.dangunmarket.domain.post.facade.dto.PostFindResponseParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostToUpdateResponseParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostsGetResponseParam;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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

    private static URI createURI(Long productId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productId)
                .toUri();
    }

}
