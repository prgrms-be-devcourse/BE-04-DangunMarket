package com.daangn.dangunmarket.domain.member.repository;

import com.daangn.dangunmarket.domain.member.model.Member;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepositoryImpl implements MemberRepository{

    private  final MemberJpaRepository memberJpaRepository;

    public MemberRepositoryImpl(MemberJpaRepository jpaRepository) {
        this.memberJpaRepository = jpaRepository;
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return memberJpaRepository.findById(memberId);
    }

    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }

}
