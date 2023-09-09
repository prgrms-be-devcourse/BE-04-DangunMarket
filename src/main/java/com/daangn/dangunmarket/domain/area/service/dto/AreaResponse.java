package com.daangn.dangunmarket.domain.area.service.dto;

import com.daangn.dangunmarket.domain.area.model.Area;

import java.time.LocalDateTime;

public record AreaResponse(
        Long areaId,
        String admCode,
        String areaName,
        LocalDateTime version
) {

    public static AreaResponse of(Area area){
        return new AreaResponse(
                area.getId(),
                area.getAdmCode(),
                area.getName(),
                area.getVersion());
    }

}
