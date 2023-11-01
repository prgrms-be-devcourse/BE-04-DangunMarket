package com.daangn.dangunmarket.domain.post.controller.dto.postlike;

public record LikedPostFindApiResponse(
        String title,
        String imageUrl,
        String areaName,
        long price,
        long likeCount
) {
}
