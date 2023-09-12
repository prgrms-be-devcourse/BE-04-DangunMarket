package com.daangn.dangunmarket.domain.member.service.dto;

import com.daangn.dangunmarket.domain.member.model.MemberProvider;
import com.daangn.dangunmarket.domain.member.model.NickName;
import com.daangn.dangunmarket.domain.member.model.RoleType;

public record MemberCreateResponse(
    Long id,
    RoleType roleType,
    MemberProvider memberProvider,
    String nickName,
    String socialId,
    Integer reviewScore) { }
