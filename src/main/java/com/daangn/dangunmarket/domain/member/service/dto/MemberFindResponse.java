package com.daangn.dangunmarket.domain.member.service.dto;

import com.daangn.dangunmarket.domain.member.model.ActivityArea;
import com.daangn.dangunmarket.domain.member.model.MemberProvider;
import com.daangn.dangunmarket.domain.member.model.RoleType;

import java.util.List;

public record MemberFindResponse(
        Long id,
        RoleType roleType,
        MemberProvider memberProvider,
        String nickName,
        String socialId,
        Integer reviewScore,
        List<ActivityArea> activityAreas) {
}
