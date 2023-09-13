package com.daangn.dangunmarket.domain.post.facade.dto;

import java.util.List;

public record PostToUpdateResponseParam (
        Long postId,
        double latitude,
        double longitude,
        String locationPreferenceAreas,
        List<String> postImages,
        Long categoryId,
        String categoryName,
        String title,
        String content,
        long price,
        boolean isOfferAllowed) {
}

