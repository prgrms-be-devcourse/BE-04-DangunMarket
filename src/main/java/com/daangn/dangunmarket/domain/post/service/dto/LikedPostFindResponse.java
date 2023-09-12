package com.daangn.dangunmarket.domain.post.service.dto;

public record LikedPostFindResponse(
        String title,
        String imageUrl,
        String areaName,
        long price,
        long likeCount
) {
}
