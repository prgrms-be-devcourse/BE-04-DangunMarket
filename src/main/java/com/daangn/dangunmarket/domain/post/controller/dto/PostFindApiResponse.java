package com.daangn.dangunmarket.domain.post.controller.dto;

import com.daangn.dangunmarket.domain.post.facade.dto.PostFindResponseParam;
import com.daangn.dangunmarket.domain.post.model.TradeStatus;

import java.time.LocalDateTime;
import java.util.List;

public record PostFindApiResponse(
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
        LocalDateTime refreshedAt
) {

    public static PostFindApiResponse from(PostFindResponseParam responseParam){
        return new PostFindApiResponse(
                responseParam.postId(),
                responseParam.memberName(),
                responseParam.areaName(),
                responseParam.latitude(),
                responseParam.longitude(),
                responseParam.LocationPreferenceAreas(),
                responseParam.urls(),
                responseParam.categoryId(),
                responseParam.categoryName(),
                responseParam.tradeStatus(),
                responseParam.title(),
                responseParam.content(),
                responseParam.price(),
                responseParam.isOfferAllowed(),
                responseParam.refreshedAt());
    }

}
