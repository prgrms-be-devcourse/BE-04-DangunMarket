package com.daangn.dangunmarket.domain.member.service;

import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.repository.MemberJpaRepository;
import com.daangn.dangunmarket.domain.member.service.dto.MemberResponse;
import com.daangn.dangunmarket.global.Exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import static com.daangn.dangunmarket.global.response.ErrorCode.NOT_FOUND_MEMBER_ENTITY;

@Service
public class MemberServiceImpl implements MemberService{

    private MemberJpaRepository memberJpaRepository;

    public MemberServiceImpl(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public MemberResponse findById(Long id) {
        Member member = memberJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MEMBER_ENTITY));

        return MemberResponse.from(member);
    }

}
