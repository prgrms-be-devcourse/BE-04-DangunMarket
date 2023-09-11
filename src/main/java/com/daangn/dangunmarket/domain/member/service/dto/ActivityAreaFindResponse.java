package com.daangn.dangunmarket.domain.member.service.dto;

import com.daangn.dangunmarket.domain.member.model.Member;

public record ActivityAreaFindResponse(
        Long id,
        Member member,
        Long emdAreaId,
        boolean isVerified) { }
