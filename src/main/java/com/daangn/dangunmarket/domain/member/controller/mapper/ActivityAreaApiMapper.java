package com.daangn.dangunmarket.domain.member.controller.mapper;

import com.daangn.dangunmarket.domain.member.controller.dto.ActivityAreaCreateApiRequest;
import com.daangn.dangunmarket.domain.member.controller.dto.ActivityAreaCreateApiResponse;
import com.daangn.dangunmarket.domain.member.controller.dto.ActivityAreaIsVerifiedApiResponse;
import com.daangn.dangunmarket.domain.member.facade.dto.ActivityAreaCreateRequestParam;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;


@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ActivityAreaApiMapper {
    ActivityAreaCreateRequestParam toActivityAreaCreateRequestParam(ActivityAreaCreateApiRequest activityAreaCreateApiRequest);

    ActivityAreaCreateApiResponse toActivityAreaCreateApiResponse(Long activityAreaId);

    ActivityAreaIsVerifiedApiResponse toActivityAreaIsVerifiedApiResponse(Boolean isVerified);

}
