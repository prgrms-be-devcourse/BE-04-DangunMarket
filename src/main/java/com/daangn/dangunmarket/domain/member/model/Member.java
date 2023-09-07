package com.daangn.dangunmarket.domain.member.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "Members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberProvider memberProvider;

    @Column(nullable = false)
    private String socialToken;

    //@Valid
    @Embedded
    @Column(nullable = false)
    private NickName nickName;

    @Column
    private Integer reviewScore;

}
