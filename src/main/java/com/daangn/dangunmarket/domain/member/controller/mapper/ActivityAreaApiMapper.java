package com.daangn.dangunmarket.domain.member.controller.mapper;

import com.daangn.dangunmarket.domain.member.controller.dto.ActivityAreaCreateApiRequest;
import com.daangn.dangunmarket.domain.member.controller.dto.ActivityAreaCreateApiResponse;
import com.daangn.dangunmarket.domain.member.facade.dto.ActivityAreaCreateRequestParam;
import com.daangn.dangunmarket.domain.member.facade.dto.ActivityAreaCreateResponseParam;
import com.daangn.dangunmarket.domain.member.service.dto.ActivityAreaCreateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring") // injectionStrategy - Constructor
public interface ActivityAreaApiMapper {
    ActivityAreaApiMapper INSTANCE = Mappers.getMapper(ActivityAreaApiMapper.class);

    ActivityAreaCreateRequestParam toActivityAreaCreateRequestParam(ActivityAreaCreateApiRequest activityAreaCreateApiRequest);

    ActivityAreaCreateApiResponse toActivityAreaCreateApiResponse(Long activityAreaId);

}
