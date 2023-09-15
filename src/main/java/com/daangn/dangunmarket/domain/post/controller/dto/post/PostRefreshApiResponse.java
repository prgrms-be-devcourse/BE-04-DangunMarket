package com.daangn.dangunmarket.domain.post.controller.dto.post;

public record PostRefreshApiResponse(
        boolean flag,
        Long postId) {

    public static PostRefreshApiResponse from(Long postId) {
        return new PostRefreshApiResponse(true, postId);
    }

}
