package com.daangn.dangunmarket.domain.chat.service.dto;

public record ChatMessagePageRequest(
        Long chatRoomId,
        int page,
        int pageSize
) {
}
