package com.daangn.dangunmarket.domain.post.service.dto;

import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.LocationPreference;
import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.model.TradeStatus;

import java.time.LocalDateTime;
import java.util.List;

public record PostFindResponse(
        Long postId,
        Long memberId,
        Long areaId,
        LocationPreference locationPreference,
        List<PostImage> postImageList,
        Category category,
        TradeStatus tradeStatus,
        String title,
        String content,
        long price,
        boolean isOfferAllowed,
        LocalDateTime refreshedAt) {

    public static PostFindResponse from(Post post){
        return new PostFindResponse(
                post.getId(),
                post.getMemberId(),
                post.getAreaId(),
                post.getLocalPreference(),
                post.getPostImageList(),
                post.getCategory(),
                post.getTradeStatus(),
                post.getTitle(),
                post.getContent(),
                post.getPrice(),
                post.isOfferAllowed(),
                post.getRefreshedAt());
    }
}
