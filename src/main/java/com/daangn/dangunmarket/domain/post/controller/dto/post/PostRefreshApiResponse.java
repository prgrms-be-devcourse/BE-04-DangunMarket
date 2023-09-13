package com.daangn.dangunmarket.domain.post.controller.dto.post;

public record PostRefreshApiResponse(Long postId) {

    public static PostRefreshApiResponse from(Long postId){
        return new PostRefreshApiResponse(postId);
    }

}
