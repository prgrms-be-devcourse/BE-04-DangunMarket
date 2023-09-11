package com.daangn.dangunmarket.domain.member.repository;

import com.daangn.dangunmarket.domain.member.model.ActivityArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ActivityAreaJpaRepository extends JpaRepository<ActivityArea, Long> {

    @Query("SELECT a FROM ActivityArea a JOIN FETCH a.member WHERE a.id = :activityId")
    Optional<ActivityArea> findByActivityId(@Param("activityId") Long activityId);

    @Query("SELECT DISTINCT a FROM ActivityArea a JOIN a.member m WHERE m.id = :memberId")
    Optional<ActivityArea> isExistedActivityAreaByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT COUNT(*) FROM ActivityArea a JOIN a.member m WHERE m.id=:memberId")
    int countActivityAreaByMemberId(Long memberId);

}
