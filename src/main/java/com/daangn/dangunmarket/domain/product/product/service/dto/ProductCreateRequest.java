package com.daangn.dangunmarket.domain.product.product.service.dto;

import com.daangn.dangunmarket.domain.category.domain.Category;
import com.daangn.dangunmarket.domain.member.member.domain.Member;
import com.daangn.dangunmarket.domain.product.locationPreference.domain.LocationPreference;
import com.daangn.dangunmarket.domain.product.productImage.domain.ProductImage;

import java.util.List;

public record ProductCreateRequest(
        Member member,
        LocationPreference locationPreference,
        List<ProductImage> productImages,
        Category category,
        String title,
        String content,
        Long price,
        boolean isOfferAllowed) {
}
