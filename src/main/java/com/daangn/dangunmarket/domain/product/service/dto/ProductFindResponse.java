package com.daangn.dangunmarket.domain.product.service.dto;

import com.daangn.dangunmarket.domain.product.model.Category;
import com.daangn.dangunmarket.domain.product.model.LocationPreference;
import com.daangn.dangunmarket.domain.product.model.Product;
import com.daangn.dangunmarket.domain.product.model.ProductImage;
import com.daangn.dangunmarket.domain.product.model.TradeStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ProductFindResponse(
        Long productId,
        Long memberId,
        Long areaId,
        LocationPreference locationPreference,
        List<ProductImage> productImageList,
        Category category,
        TradeStatus tradeStatus,
        String title,
        String content,
        long price,
        boolean isOfferAllowed,
        LocalDateTime refreshedAt) {

    public static ProductFindResponse from(Product product){
        return new ProductFindResponse(
                product.getId(),
                product.getMemberId(),
                product.getAreaId(),
                product.getLocalPreference(),
                product.getProductImageList(),
                product.getCategory(),
                product.getTradeStatus(),
                product.getTitle(),
                product.getContent(),
                product.getPrice(),
                product.isOfferAllowed(),
                product.getRefreshedAt());
    }
}
