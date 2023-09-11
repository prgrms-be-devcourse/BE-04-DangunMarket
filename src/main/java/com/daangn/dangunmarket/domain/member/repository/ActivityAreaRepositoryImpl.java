package com.daangn.dangunmarket.domain.member.repository;

import com.daangn.dangunmarket.domain.member.model.ActivityArea;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ActivityAreaRepositoryImpl implements ActivityAreaRepository{

    private final ActivityAreaJpaRepository activityAreaJpaRepository;

    public ActivityAreaRepositoryImpl(ActivityAreaJpaRepository activityAreaJpaRepository) {
        this.activityAreaJpaRepository = activityAreaJpaRepository;
    }

    @Override
    public ActivityArea save(ActivityArea activityArea) {
        return activityAreaJpaRepository.save(activityArea);
    }

    @Override
    public Optional<ActivityArea> findByActivityId(Long activityId) {
        return activityAreaJpaRepository.findByActivityId(activityId);
    }

    @Override
    public int countActivityAreaByMemberId(Long memberId) {
        return activityAreaJpaRepository.countActivityAreaByMemberId(memberId);
    }

    @Override
    public Optional<ActivityArea> isExistedActivityAreaByMemberId(Long memberId) {
        return activityAreaJpaRepository.isExistedActivityAreaByMemberId(memberId);
    }

}
