package com.daangn.dangunmarket.domain.member.service;

import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.service.dto.MemberCreateResponse;
import com.daangn.dangunmarket.domain.member.service.dto.MemberFindResponse;

public interface MemberService {

    MemberFindResponse findById(Long id);

    MemberCreateResponse save(Member member);

}
