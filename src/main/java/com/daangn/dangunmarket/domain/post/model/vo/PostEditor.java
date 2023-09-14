package com.daangn.dangunmarket.domain.post.model.vo;

import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.LocationPreference;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.service.dto.PostUpdateRequest;

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

    public static PostEditor toPostEditor(PostUpdateRequest request) {
        return new PostEditor(
                request.postId(),
                request.locationPreference(),
                request.postImages(),
                request.category(),
                request.areaId(),
                request.title(),
                request.content(),
                request.price(),
                request.isOfferAllowed());
    }

}
