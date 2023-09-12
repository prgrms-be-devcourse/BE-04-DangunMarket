package com.daangn.dangunmarket.domain.member.controller.mapper;

import com.daangn.dangunmarket.domain.member.controller.dto.ActivityAreaCreateApiRequest;
import com.daangn.dangunmarket.domain.member.controller.dto.ActivityAreaCreateApiResponse;
import com.daangn.dangunmarket.domain.member.controller.dto.ActivityAreaIsVerifiedApiResponse;
import com.daangn.dangunmarket.domain.member.facade.dto.ActivityAreaCreateRequestParam;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-12T17:06:48+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class ActivityAreaApiMapperImpl implements ActivityAreaApiMapper {

    @Override
    public ActivityAreaCreateRequestParam toActivityAreaCreateRequestParam(ActivityAreaCreateApiRequest activityAreaCreateApiRequest) {
        if ( activityAreaCreateApiRequest == null ) {
            return null;
        }

        Double latitude = null;
        Double longitude = null;

        latitude = activityAreaCreateApiRequest.latitude();
        longitude = activityAreaCreateApiRequest.longitude();

        ActivityAreaCreateRequestParam activityAreaCreateRequestParam = new ActivityAreaCreateRequestParam( latitude, longitude );

        return activityAreaCreateRequestParam;
    }

    @Override
    public ActivityAreaCreateApiResponse toActivityAreaCreateApiResponse(Long activityAreaId) {
        if ( activityAreaId == null ) {
            return null;
        }

        Long activityAreaId1 = null;

        activityAreaId1 = activityAreaId;

        ActivityAreaCreateApiResponse activityAreaCreateApiResponse = new ActivityAreaCreateApiResponse( activityAreaId1 );

        return activityAreaCreateApiResponse;
    }

    @Override
    public ActivityAreaIsVerifiedApiResponse toActivityAreaIsVerifiedApiResponse(Boolean isVerified) {
        if ( isVerified == null ) {
            return null;
        }

        boolean isVerified1 = false;

        if ( isVerified != null ) {
            isVerified1 = isVerified;
        }

        ActivityAreaIsVerifiedApiResponse activityAreaIsVerifiedApiResponse = new ActivityAreaIsVerifiedApiResponse( isVerified1 );

        return activityAreaIsVerifiedApiResponse;
    }
}
