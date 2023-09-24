package com.daangn.dangunmarket.domain.chat.service.dto;

import com.daangn.dangunmarket.domain.chat.model.MessageType;

import java.time.LocalDateTime;
import java.util.List;

public record ChatMessageResponse(
        String chatMessageId,
        Long memberId,
        Long chatRoomId,
        String message,
        List<String> imageUrls,
        Integer readOrNot,
        LocalDateTime createdAt,
        MessageType type
) {
}
