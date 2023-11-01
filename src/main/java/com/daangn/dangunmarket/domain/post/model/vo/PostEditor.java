package com.daangn.dangunmarket.domain.post.model.vo;

import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.LocationPreference;
import com.daangn.dangunmarket.domain.post.model.PostImage;

import java.util.List;

public record PostEditor(
        Long postId,
        LocationPreference locationPreference,
        List<PostImage> postImages,
        Category category,
        Long areaId,
        String title,
        String content,
        long price,
        boolean isOfferAllowed
) {
}
