package com.daangn.dangunmarket.domain.post.controller;

import com.daangn.dangunmarket.domain.auth.jwt.CustomUser;
import com.daangn.dangunmarket.domain.post.controller.dto.postlike.LikedPostFindApiResponseList;
import com.daangn.dangunmarket.domain.post.controller.mapper.PostApiMapper;
import com.daangn.dangunmarket.domain.post.service.PostLikeService;
import com.daangn.dangunmarket.domain.post.service.dto.LikedPostFindResponseList;
import com.daangn.dangunmarket.global.MemberInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 좋아요한 게시글 목록
     */
    @GetMapping("/me")
    public ResponseEntity<LikedPostFindApiResponseList> findLikedPosts(
            @MemberInfo CustomUser customUser,
            Pageable pageable) {

        LikedPostFindResponseList response = postLikeService.findByMemberId(customUser.memberId(), pageable);
        LikedPostFindApiResponseList responses = LikedPostFindApiResponseList.from(response);

        return ResponseEntity.ok(responses);
    }

}
