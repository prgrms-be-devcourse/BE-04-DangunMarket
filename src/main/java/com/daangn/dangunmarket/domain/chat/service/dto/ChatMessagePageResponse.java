package com.daangn.dangunmarket.domain.chat.service.dto;

public record ChatMessagePageResponse(
        String messageId,
        Long chatRoomId,
        String message,
        String imgUrl,
        Integer readOrNot,
        String createdAt,
        boolean isMine
) {
}
