package com.daangn.dangunmarket.domain.area.repository;

import com.daangn.dangunmarket.domain.area.model.Area;

import java.util.Optional;

public interface AreaReader {
    Optional<Area> findById(Long id);
}
