package com.daangn.dangunmarket.domain.area.repository;

import com.daangn.dangunmarket.domain.area.model.Area;
import org.springframework.stereotype.Repository;

@Repository
public class AreaReaderImpl implements AreaReader{

    private final AreaRepository areaRepository;

    public AreaReaderImpl(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    @Override
    public Area findById(Long id) {
        return areaRepository.findById(id).orElseThrow();
    }
}
