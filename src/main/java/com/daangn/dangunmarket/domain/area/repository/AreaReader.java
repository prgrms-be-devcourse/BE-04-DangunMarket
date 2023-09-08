package com.daangn.dangunmarket.domain.area.repository;

import com.daangn.dangunmarket.domain.area.model.Area;

public interface AreaReader {
    Area findById(Long id);
}
