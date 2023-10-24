package com.daangn.dangunmarket.domain.chat.service.dto;

import java.util.List;

public record ChatMessagePageResponse(
        String messageId,
        Long chatRoomId,
        String message,
        List<String> imageUrls,
        Integer readOrNot,
        String createdAt,
        boolean isMine
) {
}
