package com.daangn.dangunmarket.domain.member.service.mapper;

import com.daangn.dangunmarket.domain.member.model.ActivityArea;
import com.daangn.dangunmarket.domain.member.model.Member;
import org.springframework.stereotype.Component;

@Component
public class ActivityAreaMapper {

    public ActivityArea toEntity(Member member, Long areaId) {
        return ActivityArea.builder()
                .emdAreaId(areaId)
                .member(member)
                .build();
    }
}
