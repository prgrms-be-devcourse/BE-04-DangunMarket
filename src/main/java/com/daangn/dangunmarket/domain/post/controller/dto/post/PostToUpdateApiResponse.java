package com.daangn.dangunmarket.domain.post.controller.dto.post;

import java.util.List;

public record PostToUpdateApiResponse(
        Long postId,
        double latitude,
        double longitude,
        String locationPreferenceAlias,
        List<String> postImages,
        Long categoryId,
        String categoryName,
        String title,
        String content,
        long price,
        boolean isOfferAllowed) {
}

