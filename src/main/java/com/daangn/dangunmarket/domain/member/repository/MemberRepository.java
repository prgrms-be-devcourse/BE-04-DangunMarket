package com.daangn.dangunmarket.domain.member.repository;

import com.daangn.dangunmarket.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findBySocialToken(String socialToken);
    @Query("select m from Member m where m.socialToken= ?1")
    Optional<Member> findBySocialTokenOptional(String socialToken);
}
