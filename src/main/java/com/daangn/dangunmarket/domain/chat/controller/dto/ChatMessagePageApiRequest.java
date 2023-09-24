package com.daangn.dangunmarket.domain.chat.controller.dto;

import jakarta.validation.constraints.NotNull;

public record ChatMessagePageApiRequest (
        @NotNull Long chatRoomId,
        @NotNull int page,
        @NotNull int pageSize
) {
}
