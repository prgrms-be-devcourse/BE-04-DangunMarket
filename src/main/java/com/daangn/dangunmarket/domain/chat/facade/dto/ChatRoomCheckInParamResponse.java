package com.daangn.dangunmarket.domain.chat.facade.dto;

public record ChatRoomCheckInParamResponse (
        Long postId,
        String nickName,
        Integer reviewScore,
        String tradeState,
        String title,
        long price,
        boolean isOfferAllowed,
        String productUrl
) {
}

