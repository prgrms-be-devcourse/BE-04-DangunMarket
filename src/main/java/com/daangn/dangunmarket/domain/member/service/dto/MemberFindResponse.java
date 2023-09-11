package com.daangn.dangunmarket.domain.member.service.dto;

import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.model.MemberProvider;
import com.daangn.dangunmarket.domain.member.model.NickName;
import com.daangn.dangunmarket.domain.member.model.RoleType;
import jakarta.persistence.*;

public record MemberFindResponse(
        Long id,
        RoleType roleType,
        MemberProvider memberProvider,
        String nickName,
        String socialId,
        Integer reviewScore) { }
