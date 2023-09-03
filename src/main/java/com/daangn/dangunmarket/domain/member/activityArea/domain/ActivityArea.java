package com.daangn.dangunmarket.domain.member.activityArea.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 고치기
    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long emdAreaId;

    @Column
    private boolean isVerified;

}
