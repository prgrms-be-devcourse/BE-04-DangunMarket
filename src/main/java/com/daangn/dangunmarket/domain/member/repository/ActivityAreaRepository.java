package com.daangn.dangunmarket.domain.member.repository;

import com.daangn.dangunmarket.domain.member.model.ActivityArea;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ActivityAreaRepository {
    ActivityArea save(ActivityArea activityArea);

    Optional<ActivityArea> findByActivityId(Long activityId);

    int countActivityAreaByMemberId(Long memberId);

    Optional<ActivityArea> isExistedActivityAreaByMemberId(Long memberId);

    Optional<ActivityArea> findActivityAreaByMemberIdAndEmdAreaId(Long memberId, Long areaId);

}
