package com.daangn.dangunmarket.domain.member.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.daangn.dangunmarket.domain.member.model.ActivityArea;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.repository.ActivityAreaRepository;
import com.daangn.dangunmarket.domain.member.service.dto.ActivityAreaFindResponse;
import com.daangn.dangunmarket.domain.member.service.dto.MemberFindResponse;
import com.daangn.dangunmarket.domain.member.service.mapper.ActivityAreaDtoMapper;
import com.daangn.dangunmarket.domain.member.service.mapper.ActivityAreaMapper;
import com.daangn.dangunmarket.domain.member.service.mapper.MemberMapper;
import com.daangn.dangunmarket.global.response.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class ActivityAreaService {

    private final ActivityAreaMapper activityAreaMapper;
    private final MemberMapper memberMapper;
    private final ActivityAreaDtoMapper activityAreaDtoMapper;
    private final ActivityAreaRepository activityAreaRepository;

    public ActivityAreaService(ActivityAreaMapper activityAreaMapper, MemberMapper memberMapper, ActivityAreaDtoMapper activityAreaDtoMapper, ActivityAreaRepository activityAreaRepository) {
        this.activityAreaMapper = activityAreaMapper;
        this.memberMapper = memberMapper;
        this.activityAreaDtoMapper = activityAreaDtoMapper;
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

   public Optional<ActivityArea> isExistedActivityAreaByMemberId(Long memberId) {
        return activityAreaRepository.isExistedActivityAreaByMemberId(memberId);
   }

   @Transactional
   public boolean isVerifiedActivityArea(Long memberId, Long areaId) {
       ActivityArea activityArea = activityAreaRepository.findActivityAreaByMemberIdAndEmdAreaId(memberId, areaId)
               .orElseThrow(()-> new NotFoundException("사용자의 위도 경도는 등록된 활동 지역이 아닙니다."));

       activityArea.authorizedActivityArea();
       activityAreaRepository.save(activityArea);

       return true;
   }

}
