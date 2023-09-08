package com.daangn.dangunmarket.domain.post.controller;

import com.daangn.dangunmarket.domain.auth.jwt.CustomUser;
import com.daangn.dangunmarket.domain.post.service.PostLikeService;
import com.daangn.dangunmarket.domain.post.service.dto.PostLikeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/post-likes",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)

public class PostLikeController {

    private PostLikeService postLikeService;

    public PostLikeController(PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }

    /**
     * 게시물 좋아요 API
     *
     * @param postId
     * @param authentication
     * @return
     */
    @PostMapping(path = "/{postId}")
    public ResponseEntity<PostLikeResponse> addLikePost(
            @PathVariable Long postId,
            Authentication authentication
    ) {
        CustomUser customUser = (CustomUser) authentication.getPrincipal();

        PostLikeResponse response = postLikeService.likePost(customUser.memberId(), postId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * 게시물 좋아요 취소 API
     *
     * @param postId
     * @param authentication
     * @return
     */
    @DeleteMapping(path = "/{postId}")
    public ResponseEntity<PostLikeResponse> cancelLikePost(
            @PathVariable Long postId,
            Authentication authentication
    ) {
        CustomUser customUser = (CustomUser) authentication.getPrincipal();

        PostLikeResponse response = postLikeService.cancelLikePost(customUser.memberId(), postId);
        return ResponseEntity
                .status((HttpStatus.OK))
                .body(response);
    }

}
