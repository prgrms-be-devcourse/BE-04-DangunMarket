package com.daangn.dangunmarket.domain.post.controller.dto;

public record PostLikeApiResponse(
        int likeCount,
        boolean isLiked
) {
    public static PostLikeApiResponse of(int likeCount, boolean isLiked) {
        return new PostLikeApiResponse(
                likeCount,
                isLiked
        );
    }
}
