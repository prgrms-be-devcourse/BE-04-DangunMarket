package com.daangn.dangunmarket.global;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public final class GeometryTypeFactory {

    private static final GeometryFactory factory = new GeometryFactory();

    private GeometryTypeFactory() {
        throw new RuntimeException("유틸 클레스임!!");
    }

    public static Point createPoint(double longitude, double latitude){
        return factory.createPoint(new Coordinate(longitude, latitude));
    }

}
