package com.daangn.dangunmarket.domain.post.controller;

import com.daangn.dangunmarket.domain.auth.jwt.CustomUser;
import com.daangn.dangunmarket.domain.post.controller.dto.PostCreateApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.PostCreateApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.PostFindApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.PostToUpdateApiResponse;
import com.daangn.dangunmarket.domain.post.controller.mapper.PostApiMapper;
import com.daangn.dangunmarket.domain.post.controller.mapper.PostUpdateApiMapper;
import com.daangn.dangunmarket.domain.post.facade.PostFacade;
import com.daangn.dangunmarket.domain.post.facade.dto.PostFindResponseParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostToUpdateResponseParam;
import com.daangn.dangunmarket.domain.post.service.dto.PostToUpdateResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/posts",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class PostController {

    private final PostFacade postFacade;
    private final PostApiMapper mapper;
    private final PostUpdateApiMapper postUpdateApiMapper;

    public PostController(PostFacade postFacade, PostApiMapper mapper, PostUpdateApiMapper postUpdateApiMapper) {
        this.postFacade = postFacade;
        this.mapper = mapper;
        this.postUpdateApiMapper = postUpdateApiMapper;
    }

    @PostMapping
    public ResponseEntity<PostCreateApiResponse> createProduct(@RequestBody @Valid PostCreateApiRequest request) {
        Long productId = postFacade.createPost(mapper.toPostCreateRequest(request));
        PostCreateApiResponse response = PostCreateApiResponse.from(productId);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productId)
                .toUri();

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

        return ResponseEntity.ok(postUpdateApiMapper.toPostToUpdateApiResponse(postInfoToUpdate));
    }

}
