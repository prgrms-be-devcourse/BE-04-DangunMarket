package com.daangn.dangunmarket.domain.post.service.dto;

import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.LocationPreference;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.model.TradeStatus;

import java.util.List;

public record PostToUpdateResponse(
        Long postId,
        LocationPreference locationPreference,
        List<PostImage> postImages,
        Category category,
        TradeStatus tradeStatus,
        String title,
        String content,
        long price,
        boolean isOfferAllowed ) {
}
