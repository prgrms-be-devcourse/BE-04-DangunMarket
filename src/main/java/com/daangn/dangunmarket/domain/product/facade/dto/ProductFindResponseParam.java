package com.daangn.dangunmarket.domain.product.facade.dto;

import com.daangn.dangunmarket.domain.product.model.TradeStatus;
import com.daangn.dangunmarket.domain.product.service.dto.ProductFindResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ProductFindResponseParam(
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

    public static ProductFindResponseParam of(ProductFindResponse response, String memberName, String areaName){
        List<String> urls = response.productImageList().stream()
                .map(e -> e.getUrl())
                .collect(Collectors.toList());

        return new ProductFindResponseParam(
                response.productId(),
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
                response.refreshedAt());
    }

}
