package com.daangn.dangunmarket.domain.member.service.mapper;

import com.daangn.dangunmarket.domain.member.model.ActivityArea;
import com.daangn.dangunmarket.domain.member.service.dto.ActivityAreaCreateResponse;
import com.daangn.dangunmarket.domain.member.service.dto.ActivityAreaFindResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ActivityAreaDtoMapper {
    @Mapping(target = "isVerified", ignore = true)
    ActivityAreaFindResponse toActivityAreaFindResponse(ActivityArea activityArea);

}
