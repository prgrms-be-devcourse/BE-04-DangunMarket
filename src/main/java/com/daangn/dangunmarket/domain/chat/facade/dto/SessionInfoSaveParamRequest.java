package com.daangn.dangunmarket.domain.chat.facade.dto;

public record SessionInfoSaveParamRequest(
        String sessionId,
        Long roomId,
        Long memberId
) {
}
