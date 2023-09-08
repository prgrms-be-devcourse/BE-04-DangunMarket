package com.daangn.dangunmarket.domain.product.service.dto;


import com.daangn.dangunmarket.domain.product.model.Category;
import com.daangn.dangunmarket.domain.product.model.LocationPreference;
import com.daangn.dangunmarket.domain.product.model.ProductImage;

import java.util.List;

public record ProductCreateRequest(
        Long memberId,
        Long areaId,
        LocationPreference locationPreference,
        List<ProductImage> productImages,
        Category category,
        String title,
        String content,
        Long price,
        boolean isOfferAllowed) {
}
