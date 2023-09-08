package com.daangn.dangunmarket.domain.post.controller.dto;

public record PostCreateApiResponse(Long memberId) {
    public static PostCreateApiResponse from(Long memberId) {
        return new PostCreateApiResponse(memberId);
    }
}
