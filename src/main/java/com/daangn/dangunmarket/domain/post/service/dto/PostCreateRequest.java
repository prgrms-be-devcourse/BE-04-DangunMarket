package com.daangn.dangunmarket.domain.post.service.dto;


import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.LocationPreference;
import com.daangn.dangunmarket.domain.post.model.PostImage;

import java.util.List;

public record PostCreateRequest(
        Long memberId,
        Long areaId,
        LocationPreference locationPreference,
        List<PostImage> postImages,
        Category category,
        String title,
        String content,
        Long price,
        boolean isOfferAllowed) {
}
