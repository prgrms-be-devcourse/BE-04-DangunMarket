package com.daangn.dangunmarket.domain.member.controller.dto;

import jakarta.validation.constraints.NotNull;

public record ActivityAreaCreateApiRequest(
        @NotNull(message = "위도가 누락되었습니다.") Double latitude,
        @NotNull(message = "경도가 누락되었습니다.") Double longitude) { }
