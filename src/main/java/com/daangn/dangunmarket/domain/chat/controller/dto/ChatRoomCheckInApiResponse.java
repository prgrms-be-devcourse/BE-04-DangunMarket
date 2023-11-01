package com.daangn.dangunmarket.domain.chat.controller.dto;

public record ChatRoomCheckInApiResponse(
        Long postId,
        String nickName,
        Integer reviewScore,
        String tradeStatus,
        String title,
        long price,
        boolean isOfferAllowed,
        String productImageUrl
) {
}
