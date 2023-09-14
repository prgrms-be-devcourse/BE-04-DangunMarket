package com.daangn.dangunmarket.domain.member.controller.mapper;

import com.daangn.dangunmarket.domain.member.controller.dto.ActivityAreaCreateApiRequest;
import com.daangn.dangunmarket.domain.member.controller.dto.ActivityAreaCreateApiResponse;
import com.daangn.dangunmarket.domain.member.controller.dto.ActivityAreaIsVerifiedApiRequest;
import com.daangn.dangunmarket.domain.member.controller.dto.ActivityAreaIsVerifiedApiResponse;
import com.daangn.dangunmarket.domain.member.facade.dto.ActivityAreaCreateRequestParam;
import com.daangn.dangunmarket.domain.member.facade.dto.ActivityAreaIsVerifiedRequestParam;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;


@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ActivityAreaApiMapper {
    ActivityAreaCreateRequestParam toActivityAreaCreateRequestParam(ActivityAreaCreateApiRequest activityAreaCreateApiRequest);

    ActivityAreaIsVerifiedRequestParam toActivityAreaIsVerifiedRequestParam(ActivityAreaIsVerifiedApiRequest request);

    ActivityAreaCreateApiResponse toActivityAreaCreateApiResponse(Long activityAreaId);

    ActivityAreaIsVerifiedApiResponse toActivityAreaIsVerifiedApiResponse(Boolean isVerified);

}
