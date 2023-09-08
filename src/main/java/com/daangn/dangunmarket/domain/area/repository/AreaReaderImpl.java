package com.daangn.dangunmarket.domain.area.repository;

import com.daangn.dangunmarket.domain.area.model.Area;
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

}
