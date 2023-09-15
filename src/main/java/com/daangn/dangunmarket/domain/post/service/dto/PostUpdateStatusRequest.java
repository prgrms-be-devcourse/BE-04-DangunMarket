package com.daangn.dangunmarket.domain.post.service.dto;

import com.daangn.dangunmarket.domain.post.model.TradeStatus;

public record PostUpdateStatusRequest(
        TradeStatus tradeStatus,
        Long postId
) {
}
