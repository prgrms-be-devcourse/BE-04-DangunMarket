package com.daangn.dangunmarket.domain.member.service.mapper;

import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.model.MemberProvider;
import com.daangn.dangunmarket.domain.member.model.RoleType;
import com.daangn.dangunmarket.domain.member.service.dto.MemberCreateResponse;
import com.daangn.dangunmarket.domain.member.service.dto.MemberFindResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-12T17:06:48+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class MemberDtoMapperImpl implements MemberDtoMapper {

    @Override
    public MemberFindResponse toMemberFindResponse(Member member) {
        if ( member == null ) {
            return null;
        }

        Long id = null;
        RoleType roleType = null;
        MemberProvider memberProvider = null;
        String nickName = null;
        String socialId = null;
        Integer reviewScore = null;

        id = member.getId();
        roleType = member.getRoleType();
        memberProvider = member.getMemberProvider();
        nickName = member.getNickName();
        socialId = member.getSocialId();
        reviewScore = member.getReviewScore();

        MemberFindResponse memberFindResponse = new MemberFindResponse( id, roleType, memberProvider, nickName, socialId, reviewScore );

        return memberFindResponse;
    }

    @Override
    public MemberCreateResponse toMemberCreateResponse(Member member) {
        if ( member == null ) {
            return null;
        }

        Long id = null;
        RoleType roleType = null;
        MemberProvider memberProvider = null;
        String nickName = null;
        String socialId = null;
        Integer reviewScore = null;

        id = member.getId();
        roleType = member.getRoleType();
        memberProvider = member.getMemberProvider();
        nickName = member.getNickName();
        socialId = member.getSocialId();
        reviewScore = member.getReviewScore();

        MemberCreateResponse memberCreateResponse = new MemberCreateResponse( id, roleType, memberProvider, nickName, socialId, reviewScore );

        return memberCreateResponse;
    }
}
