package com.daangn.dangunmarket.domain.member.service.mapper;

import com.daangn.dangunmarket.domain.member.model.ActivityArea;
import com.daangn.dangunmarket.domain.member.service.dto.ActivityAreaCreateResponse;
import com.daangn.dangunmarket.domain.member.service.dto.ActivityAreaFindResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ActivityAreaDtoMapper {
    ActivityAreaDtoMapper INSTANCE = Mappers.getMapper(ActivityAreaDtoMapper.class);

    ActivityAreaFindResponse toActivityAreaFindResponse(ActivityArea activityArea);

}
