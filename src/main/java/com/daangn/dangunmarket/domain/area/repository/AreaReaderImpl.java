package com.daangn.dangunmarket.domain.area.repository;

import com.daangn.dangunmarket.domain.area.model.Area;
import com.daangn.dangunmarket.global.Exception.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import static com.daangn.dangunmarket.global.response.ErrorCode.NOT_FOUND_AREA_ENTITY;

@Repository
public class AreaReaderImpl implements AreaReader {

    private final AreaJpaRepository areaJpaRepository;

    public AreaReaderImpl(AreaJpaRepository areaJpaRepository) {
        this.areaJpaRepository = areaJpaRepository;
    }

    @Override
    public Area findById(Long id) {
        return areaJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_AREA_ENTITY));
    }
}
