package com.daangn.dangunmarket.domain.post.service.dto;

import com.daangn.dangunmarket.domain.post.model.TradeStatus;

import java.time.LocalDateTime;

public record PostGetResponse(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime refreshedAt,
        Long price,
        Integer likeCount,
        TradeStatus tradeStatus,
        String firstImageUrl,
        String postAreaName
) {
}
