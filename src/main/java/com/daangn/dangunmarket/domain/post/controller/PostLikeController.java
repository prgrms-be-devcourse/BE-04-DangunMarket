package com.daangn.dangunmarket.domain.post.controller;

import com.daangn.dangunmarket.domain.auth.jwt.CustomUser;
import com.daangn.dangunmarket.domain.post.controller.dto.PostLikeApiResponse;
import com.daangn.dangunmarket.domain.post.controller.mapper.PostApiMapper;
import com.daangn.dangunmarket.domain.post.service.PostLikeService;
import com.daangn.dangunmarket.domain.post.service.dto.PostLikeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/post-likes",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class PostLikeController {

    private final PostLikeService postLikeService;
    private final PostApiMapper mapper;

    public PostLikeController(PostLikeService postLikeService, PostApiMapper mapper) {
        this.postLikeService = postLikeService;
        this.mapper = mapper;
    }

    /**
     * 게시물 좋아요 API
     *
     * @param postId
     * @param authentication
     * @return ResponseEntity<PostLikeApiResponse>
     */
    @PostMapping(path = "/{postId}")
    public ResponseEntity<PostLikeApiResponse> addLikePost(
            @PathVariable Long postId,
            Authentication authentication
    ) {
        CustomUser customUser = (CustomUser) authentication.getPrincipal();

        PostLikeResponse response = postLikeService.likePost(customUser.memberId(), postId);
        PostLikeApiResponse apiResponse = mapper.toPostLikeApiResponse(response);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiResponse);
    }

    /**
     * 게시물 좋아요 취소 API
     *
     * @param postId
     * @param authentication
     * @return ResponseEntity<PostLikeApiResponse>
     */
    @DeleteMapping(path = "/{postId}")
    public ResponseEntity<PostLikeApiResponse> cancelLikePost(
            @PathVariable Long postId,
            Authentication authentication
    ) {
        CustomUser customUser = (CustomUser) authentication.getPrincipal();

        PostLikeResponse response = postLikeService.cancelLikePost(customUser.memberId(), postId);
        PostLikeApiResponse apiResponse = mapper.toPostLikeApiResponse(response);

        return ResponseEntity
                .status((HttpStatus.OK))
                .body(apiResponse);
    }

}
