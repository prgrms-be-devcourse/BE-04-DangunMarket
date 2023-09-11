package com.daangn.dangunmarket.domain.area.repository;

import com.amazonaws.services.kms.model.NotFoundException;
import com.daangn.dangunmarket.domain.area.model.Area;
import com.daangn.dangunmarket.global.response.ErrorCode;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AreaReaderImpl implements AreaReader {

    private final AreaJpaRepository areaJpaRepository;

    public AreaReaderImpl(AreaJpaRepository areaJpaRepository) {
        this.areaJpaRepository = areaJpaRepository;
    }

    @Override
    public Optional<Area> findById(Long id) {
        return areaJpaRepository.findById(id);
    }

    @Override
    public Long findAreaIdByPolygon(Point point) {
        Area area = areaJpaRepository.findAreasContainingPoint(point)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_ENTITY.getMessage()));

        return area.getId();
    }

}
