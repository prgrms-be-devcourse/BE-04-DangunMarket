package com.daangn.dangunmarket.domain.chat.controller.dro;

import java.time.LocalDateTime;

public record ChatRoomsFindApiResponse(
        Long chatRoomId,
        String otherMemberName,
        String latestMessage,
        Integer readOrNot,
        LocalDateTime createdAt
) {
}
