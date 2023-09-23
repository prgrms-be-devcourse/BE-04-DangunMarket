package com.daangn.dangunmarket.domain.chat.facade.dto;

public record SessionInfoSaveFacaRequest(
        String sessionId,
        Long roomId,
        Long memberId
) {
}
