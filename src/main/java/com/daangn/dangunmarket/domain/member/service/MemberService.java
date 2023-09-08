package com.daangn.dangunmarket.domain.member.service;

import com.daangn.dangunmarket.domain.member.service.dto.MemberResponse;

public interface MemberService {
    MemberResponse findById(Long id);
}
