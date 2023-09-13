package com.daangn.dangunmarket.domain.post.controller.dto.post;

public record PostCreateApiResponse(Long postId) {
    public static PostCreateApiResponse from(Long postId) {
        return new PostCreateApiResponse(postId);
    }
}
