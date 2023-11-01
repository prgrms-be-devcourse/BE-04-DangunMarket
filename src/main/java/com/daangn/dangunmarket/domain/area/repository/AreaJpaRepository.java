package com.daangn.dangunmarket.domain.area.repository;

import com.daangn.dangunmarket.domain.area.model.Area;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AreaJpaRepository extends JpaRepository<Area, Long> {

    @Query("SELECT an " +
            "FROM Area an " +
            "WHERE ST_Contains(an.geom, ?1) = true")
    Optional<Area> findAreasContainingPoint(Point point);

}
