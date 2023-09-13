package com.daangn.dangunmarket.domain.post.controller;

import com.daangn.dangunmarket.domain.auth.jwt.CustomUser;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostUpdateStatusApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostUpdateStatusApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostCreateApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostCreateApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostFindApiResponse;
import com.daangn.dangunmarket.domain.post.controller.mapper.PostApiMapper;
import com.daangn.dangunmarket.domain.post.facade.PostFacade;
import com.daangn.dangunmarket.domain.post.facade.dto.PostFindResponseParam;
import com.daangn.dangunmarket.domain.post.service.PostService;

import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
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
    private final PostService postService;
    private final PostApiMapper mapper;

    public PostController(PostFacade postFacade, PostService postService, PostApiMapper mapper) {
        this.postFacade = postFacade;
        this.postService = postService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<PostCreateApiResponse> createProduct(
            @ModelAttribute @Valid PostCreateApiRequest request,
            Authentication authentication
    ) {
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        Long postId = postFacade.createPost(mapper.toPostCreateRequestParam(request, customUser.memberId()));
        PostCreateApiResponse response = PostCreateApiResponse.from(postId);

        URI uri = createURI(postId);

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostFindApiResponse> findById(@PathVariable Long postId) {
        PostFindResponseParam responseParam = postFacade.findById(postId);
        PostFindApiResponse response = PostFindApiResponse.from(responseParam);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostUpdateStatusApiResponse> changeStatus(
            @PathVariable Long postId,
            @RequestBody PostUpdateStatusApiRequest request){

        Long responsePostId = postService.changeStatus(mapper.toPostUpdateStatusRequest(request, postId));
        PostUpdateStatusApiResponse apiResponse = PostUpdateStatusApiResponse.from(responsePostId);

        return ResponseEntity.ok(apiResponse);
    }

    private static URI createURI(Long productId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productId)
                .toUri();
    }

}
