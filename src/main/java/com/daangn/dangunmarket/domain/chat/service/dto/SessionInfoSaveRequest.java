package com.daangn.dangunmarket.domain.chat.service.dto;

public record SessionInfoSaveRequest(
        String sessionId,
        Long roomId,
        Long memberId
) {
}
