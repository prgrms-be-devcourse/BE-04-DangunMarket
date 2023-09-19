package com.daangn.dangunmarket.domain.post.controller.dto.post;

public record PostUpdateStatusApiResponse(Long postId) {

    public static PostUpdateStatusApiResponse from(Long postId){
        return new PostUpdateStatusApiResponse(postId);
    }

}
