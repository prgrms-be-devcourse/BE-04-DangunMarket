package com.daangn.dangunmarket.domain.product.controller.dto;

import com.daangn.dangunmarket.domain.product.facade.dto.ProductFindResponseParam;
import com.daangn.dangunmarket.domain.product.model.TradeStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ProductFindApiResponse(
        Long productId,
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

    public static ProductFindApiResponse from(ProductFindResponseParam responseParam){
        return new ProductFindApiResponse(
                responseParam.productId(),
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
