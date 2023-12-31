package com.daangn.dangunmarket.domain.chat.service.dto;

import java.time.LocalDateTime;

public record ChatRoomsFindResponse(
        Long chatRoomId,
        String otherMemberName,
        String latestMessage,
        Integer readOrNot,
        LocalDateTime createdAt
) {
}
