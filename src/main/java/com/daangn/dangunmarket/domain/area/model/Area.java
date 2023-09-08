package com.daangn.dangunmarket.domain.area.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.MultiPolygon;

import java.time.LocalDateTime;

@Entity
@Table(name = "areas")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String admCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private MultiPolygon geom;

    private LocalDateTime version;

    @Builder
    public Area(String admCode, String name, MultiPolygon geom, LocalDateTime version) {
        this.admCode = admCode;
        this.name = name;
        this.geom = geom;
        this.version = version;
    }
}
