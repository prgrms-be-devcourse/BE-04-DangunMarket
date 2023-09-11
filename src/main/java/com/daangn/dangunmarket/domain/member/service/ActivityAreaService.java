package com.daangn.dangunmarket.domain.member.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.daangn.dangunmarket.domain.member.model.ActivityArea;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.repository.ActivityAreaRepository;
import com.daangn.dangunmarket.domain.member.repository.MemberRepository;
import com.daangn.dangunmarket.domain.member.service.dto.ActivityAreaFindResponse;
import com.daangn.dangunmarket.domain.member.service.dto.MemberFindResponse;
import com.daangn.dangunmarket.domain.member.service.mapper.ActivityAreaDtoMapper;
import com.daangn.dangunmarket.domain.member.service.mapper.ActivityAreaMapper;
import com.daangn.dangunmarket.domain.member.service.mapper.MemberMapper;
import com.daangn.dangunmarket.global.response.ErrorCode;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class ActivityAreaService {

    private final ActivityAreaMapper activityAreaMapper;
    private final MemberMapper memberMapper;
    private final ActivityAreaDtoMapper activityAreaDtoMapper;
    private final MemberRepository memberRepository;
    private final ActivityAreaRepository activityAreaRepository;

    public ActivityAreaService(ActivityAreaMapper activityAreaMapper, MemberMapper memberMapper, ActivityAreaDtoMapper activityAreaDtoMapper, MemberRepository memberRepository, ActivityAreaRepository activityAreaRepository) {
        this.activityAreaMapper = activityAreaMapper;
        this.memberMapper = memberMapper;
        this.activityAreaDtoMapper = activityAreaDtoMapper;
        this.memberRepository = memberRepository;
        this.activityAreaRepository = activityAreaRepository;
    }

    @Transactional
    public Long createActivityArea(MemberFindResponse memberFindResponse, Long areaId) {

        Member findMember = memberMapper.toEntity(memberFindResponse);

        Optional<ActivityArea> existedActivityArea = isExistedActivityAreaByMemberId(findMember.getId());
        if(existedActivityArea.isPresent()) {
            ActivityArea activityArea = existedActivityArea.get();
            activityArea.changeAreaId(areaId);
            activityAreaRepository.save(activityArea);

            return activityArea.getId();
        }

        ActivityArea activityArea = ActivityArea.builder()
                .emdAreaId(areaId)
                .build();
        activityArea.addMember(findMember);

        return activityAreaRepository.save(activityArea).getId();

    }

    public ActivityAreaFindResponse findByActivityId(Long activityAreaId) {
        ActivityArea activityArea = activityAreaRepository.findByActivityId(activityAreaId)
                .orElseThrow(()-> new NotFoundException(ErrorCode.NOT_FOUND_ENTITY.getMessage()));

        return activityAreaDtoMapper.toActivityAreaFindResponse(activityArea);
    }

    public int countActivityAreaByMemberId(Long memberId) {
        return activityAreaRepository.countActivityAreaByMemberId(memberId);
    }

   public Optional<ActivityArea> isExistedActivityAreaByMemberId( Long memberId) {
        return activityAreaRepository.isExistedActivityAreaByMemberId(memberId);
   }

}
