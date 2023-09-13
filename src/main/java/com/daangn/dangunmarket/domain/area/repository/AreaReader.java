package com.daangn.dangunmarket.domain.area.repository;

import com.daangn.dangunmarket.domain.area.model.Area;
import org.locationtech.jts.geom.Point;
import java.util.Optional;

public interface AreaReader {
    Optional<Area> findById(Long id);

    Long findAreaIdByPolygon(Point point);

    Area save(Area area);

}
