package com.daangn.dangunmarket.domain.chat.repository.chatmessage.dto;

public record ChatMessagePageDto(
        Long chatRoomId,
        int page,
        int pageSize
) {

}
