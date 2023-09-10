package com.daangn.dangunmarket.domain.post.service.dto;

public record PostLikeResponse(
        int likeCount,
        boolean isLiked
) {
    public static PostLikeResponse of(int likeCount, boolean isLiked) {
        return new PostLikeResponse(
                likeCount,
                isLiked
        );
    }
}
