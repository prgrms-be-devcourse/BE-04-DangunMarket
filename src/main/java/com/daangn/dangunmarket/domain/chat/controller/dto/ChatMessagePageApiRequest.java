package com.daangn.dangunmarket.domain.chat.controller.dto;

public record ChatMessagePageApiRequest (
        Long chatRoomId,
        int page,
        int pageSize
) {
}
