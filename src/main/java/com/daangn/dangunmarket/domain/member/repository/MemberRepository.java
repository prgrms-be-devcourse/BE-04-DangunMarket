package com.daangn.dangunmarket.domain.member.repository;

import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.post.model.Post;

import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findById(Long memberId);

    Member save(Member member);

}
