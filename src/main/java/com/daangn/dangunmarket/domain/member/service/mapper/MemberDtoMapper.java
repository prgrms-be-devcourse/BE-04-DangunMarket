package com.daangn.dangunmarket.domain.member.service.mapper;

import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.service.dto.MemberCreateResponse;
import com.daangn.dangunmarket.domain.member.service.dto.MemberFindResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MemberDtoMapper {
    MemberDtoMapper INSTANCE = Mappers.getMapper(MemberDtoMapper.class);

    MemberFindResponse toMemberFindResponse(Member member);

    MemberCreateResponse toMemberCreateResponse(Member member);

}
