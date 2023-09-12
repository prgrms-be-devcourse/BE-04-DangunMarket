package com.daangn.dangunmarket.domain.member.service;

import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.repository.MemberRepository;
import com.daangn.dangunmarket.domain.member.service.dto.MemberCreateResponse;
import com.daangn.dangunmarket.domain.member.service.dto.MemberFindResponse;
import com.daangn.dangunmarket.domain.member.service.mapper.MemberDtoMapper;
import com.daangn.dangunmarket.global.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.daangn.dangunmarket.global.response.ErrorCode.NOT_FOUND_MEMBER_ENTITY;

@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberDtoMapper mapper;

    public MemberServiceImpl(MemberRepository memberRepository, MemberDtoMapper mapper) {
        this.memberRepository = memberRepository;
        this.mapper = mapper;
    }

    @Override
    public MemberFindResponse findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MEMBER_ENTITY));

        return mapper.toMemberFindResponse(member);
    }

    @Transactional
    @Override
    public MemberCreateResponse save(Member member) {
        return mapper.toMemberCreateResponse(memberRepository.save(member));
    }

}
