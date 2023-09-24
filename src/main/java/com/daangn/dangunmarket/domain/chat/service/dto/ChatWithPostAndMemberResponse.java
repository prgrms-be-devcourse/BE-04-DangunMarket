package com.daangn.dangunmarket.domain.chat.service.dto;

import com.daangn.dangunmarket.domain.post.model.TradeStatus;

public record ChatWithPostAndMemberResponse (
        Long postId,
        String productImageUrl,
        TradeStatus tradeStatus,
        String title,
        long price,
        boolean isOfferAllowed,
        String nickName,
        Integer reviewScore
){
}
