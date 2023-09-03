package com.daangn.dangunmarket.domain.product.locationPreference.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "location_preferences")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocationPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;

    @Column(nullable = false)
    private String alias;
}
