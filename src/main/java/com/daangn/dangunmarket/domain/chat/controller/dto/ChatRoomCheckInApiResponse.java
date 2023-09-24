package com.daangn.dangunmarket.domain.chat.controller.dto;

public record ChatRoomCheckInApiResponse(
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
