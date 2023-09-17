package com.daangn.dangunmarket.domain.chat.controller.dro;

import java.time.LocalDateTime;

public record ChatRoomsFindApiResponse(
        Long chatRoomInfoId,
        String otherMemberName,
        String latestMessage,
        boolean readOrNot,
        LocalDateTime createdAt
) {
}
