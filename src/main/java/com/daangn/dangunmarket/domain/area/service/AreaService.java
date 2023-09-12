package com.daangn.dangunmarket.domain.area.service;

import com.daangn.dangunmarket.domain.area.model.Area;
import com.daangn.dangunmarket.domain.area.repository.AreaReader;
import com.daangn.dangunmarket.domain.area.service.dto.AreaResponse;
import com.daangn.dangunmarket.global.Exception.EntityNotFoundException;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.daangn.dangunmarket.global.response.ErrorCode.NOT_FOUND_AREA_ENTITY;

@Transactional(readOnly = true)
@Service
public class AreaService {

    private AreaReader areaReader;

    public AreaService(AreaReader areaReader) {
        this.areaReader = areaReader;
    }

    public AreaResponse findById(Long areaId){
        Area area = areaReader.findById(areaId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_AREA_ENTITY));

        return AreaResponse.of(area);
    }

    public Long findAreaIdByPolygon(Point point) {
        return areaReader.findAreaIdByPolygon(point);
    }

}
