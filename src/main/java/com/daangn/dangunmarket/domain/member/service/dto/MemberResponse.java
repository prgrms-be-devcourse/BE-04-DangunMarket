package com.daangn.dangunmarket.domain.member.service.dto;

import com.daangn.dangunmarket.domain.member.model.ActivityArea;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.model.RoleType;

import java.util.List;

public record MemberResponse(
        Long memberId,
        RoleType role,
        String nickName,
        Integer reviewScore,
        List<ActivityArea> activityAreas) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(),
                member.getRoleType(),
                member.getNickName(),
                member.getReviewScore(),
                member.getActivityArea()
        );
    }

}
