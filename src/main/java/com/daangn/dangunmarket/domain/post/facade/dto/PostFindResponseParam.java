package com.daangn.dangunmarket.domain.post.facade.dto;

import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.model.TradeStatus;
import com.daangn.dangunmarket.domain.post.service.dto.PostFindResponse;

import java.time.LocalDateTime;
import java.util.List;

public record PostFindResponseParam(
        Long postId,
        String memberName,
        String areaName,
        double latitude,
        double longitude,
        String LocationPreferenceAreas,
        List<String> urls,
        Long categoryId,
        String categoryName,
        TradeStatus tradeStatus,
        String title,
        String content,
        long price,
        boolean isOfferAllowed,
        LocalDateTime refreshedAt,
        Integer likeCount
) {

    public static PostFindResponseParam of(PostFindResponse response, String memberName, String areaName) {
        List<String> urls = response.postImageList().stream()
                .map(PostImage::getUrl)
                .toList();

        return new PostFindResponseParam(
                response.postId(),
                memberName,
                areaName,
                response.locationPreference().getLatitude(),
                response.locationPreference().getLongitude(),
                response.locationPreference().getAlias(),
                urls,
                response.category().getId(),
                response.category().getName(),
                response.tradeStatus(),
                response.title(),
                response.content(),
                response.price(),
                response.isOfferAllowed(),
                response.refreshedAt(),
                response.likeCount());
    }

}
