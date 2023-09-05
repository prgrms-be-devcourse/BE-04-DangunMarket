package com.daangn.dangunmarket.domain.product.locationPreference.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.util.Assert;

@Entity
@Table(name = "location_preferences")
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
}
