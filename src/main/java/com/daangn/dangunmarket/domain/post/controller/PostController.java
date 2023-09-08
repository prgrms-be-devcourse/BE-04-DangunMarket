package com.daangn.dangunmarket.domain.post.controller;

import com.daangn.dangunmarket.domain.post.controller.dto.PostCreateApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.PostCreateApiResponse;
import com.daangn.dangunmarket.domain.post.controller.mapper.PostApiMapper;
import com.daangn.dangunmarket.domain.post.facade.PostFacade;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/posts",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class PostController {

    private PostFacade postFacade;
    private PostApiMapper mapper;

    public PostController(PostFacade postFacade, PostApiMapper mapper) {
        this.postFacade = postFacade;
        this.mapper = mapper;
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

}
