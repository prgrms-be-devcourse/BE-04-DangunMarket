package com.daangn.dangunmarket.domain.member.service.dto;

import jakarta.validation.constraints.NotNull;

public record ActivityAreaCreateRequest (
        Double latitude,
        Double longitude ){ }
