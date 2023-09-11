package com.daangn.dangunmarket.domain.member.facade.dto;

import jakarta.validation.constraints.NotNull;

public record ActivityAreaIsVerifiedRequestParam (
        @NotNull(message = "위도가 누락되었습니다.") Double latitude,
        @NotNull(message = "경도가 누락되었습니다.") Double longitude) { }
