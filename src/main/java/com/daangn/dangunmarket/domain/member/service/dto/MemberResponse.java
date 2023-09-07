package com.daangn.dangunmarket.domain.member.service.dto;

import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.model.RoleType;

public record MemberResponse(
        Long memberId,
        RoleType role,
        String nickName,
        Integer reviewScore) {

    public static MemberResponse from(Member member){
        return new MemberResponse(member.getId(),
                member.getRole(),
                member.getNickName(),
                member.getReviewScore());
    }

}
