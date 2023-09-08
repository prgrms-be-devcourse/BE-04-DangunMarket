package com.daangn.dangunmarket.domain.post.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import org.springframework.util.Assert;

@Entity
@Table(name = "location_preferences")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "GEOMETRY")
    private Point geography;

    @Column(nullable = false)
    private String alias;

    public LocationPreference(Point geography, String alias) {
        Assert.notNull(geography, "geography는 null값이 들어올 수 없습니다.");
        Assert.notNull(alias, "alias는 null값이 들어올 수 없습니다.");

        this.geography = geography;
        this.alias = alias;
    }

    public double getLatitude() {
        return geography.getY();
    }

    public double getLongitude(){
        return geography.getX();
    }

}
