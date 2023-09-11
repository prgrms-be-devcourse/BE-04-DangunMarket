package com.daangn.dangunmarket.domain.member.service.mapper;

import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.model.NickName;
import com.daangn.dangunmarket.domain.member.service.dto.MemberFindResponse;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public Member toEntity(MemberFindResponse response) {
        return Member.builder()
                .id(response.id())
                .memberProvider(response.memberProvider())
                .reviewScore(response.reviewScore())
                .roleType(response.roleType())
                .socialId(response.socialId())
                .nickName(new NickName(response.nickName()))
                .build();
    }

}
