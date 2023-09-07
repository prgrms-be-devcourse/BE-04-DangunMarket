package com.daangn.dangunmarket.domain.member.service.dto;

import com.daangn.dangunmarket.domain.member.model.NickName;
import com.daangn.dangunmarket.domain.member.model.RoleType;

public record MemberResponse(
        Long chatInformationId,
        Long messageId,
        Long activityAreasId,
        RoleType role,
        NickName nickName,
        Integer reviewScore) {
}
