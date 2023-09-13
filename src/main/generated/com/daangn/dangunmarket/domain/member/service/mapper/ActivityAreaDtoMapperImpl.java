package com.daangn.dangunmarket.domain.member.service.mapper;

import com.daangn.dangunmarket.domain.member.model.ActivityArea;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.service.dto.ActivityAreaFindResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-12T17:06:47+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class ActivityAreaDtoMapperImpl implements ActivityAreaDtoMapper {

    @Override
    public ActivityAreaFindResponse toActivityAreaFindResponse(ActivityArea activityArea) {
        if ( activityArea == null ) {
            return null;
        }

        Long id = null;
        Member member = null;
        Long emdAreaId = null;

        id = activityArea.getId();
        member = activityArea.getMember();
        emdAreaId = activityArea.getEmdAreaId();

        Boolean isVerified = null;

        ActivityAreaFindResponse activityAreaFindResponse = new ActivityAreaFindResponse( id, member, emdAreaId, isVerified );

        return activityAreaFindResponse;
    }
}
