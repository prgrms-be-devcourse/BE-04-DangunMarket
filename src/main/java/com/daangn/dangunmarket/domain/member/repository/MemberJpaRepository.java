package com.daangn.dangunmarket.domain.member.repository;

import com.daangn.dangunmarket.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(Long memberId);

    Member findBySocialId(String socialId);

    @Query("select m from Member m where m.socialId= ?1")
    Optional<Member> findMemberIfExisted(String socialId);

}
