package com.daangn.dangunmarket.domain.post.service.dto;

import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.LocationPreference;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.model.vo.PostEditor;

import java.util.List;

public record PostUpdateRequest (
        Long postId,
        LocationPreference locationPreference,
        List<PostImage> postImages,
        List<String> urls,
        Category category,
        Long areaId,
        String title,
        String content,
        long price,
        boolean isOfferAllowed) {

    public PostEditor toPostEditor() {
        return new PostEditor(
                this.postId(),
                this.locationPreference(),
                this.postImages(),
                this.category(),
                this.areaId(),
                this.title(),
                this.content(),
                this.price(),
                this.isOfferAllowed());
    }
}

