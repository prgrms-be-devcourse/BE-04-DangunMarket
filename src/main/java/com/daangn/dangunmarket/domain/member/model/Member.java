package com.daangn.dangunmarket.domain.member.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "Members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType roleType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberProvider memberProvider;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<ActivityArea> activityAreas = new ArrayList<>();

    @Column(nullable = false)
    private String socialId;

    @Embedded
    @Column(nullable = false)
    private NickName nickName;

    @Column
    private Integer reviewScore;

    @Builder
    public Member(Long id, RoleType roleType, MemberProvider memberProvider, String socialId, NickName nickName, Integer reviewScore) {
        this.id = id;
        this.roleType = roleType;
        this.memberProvider = memberProvider;
        this.socialId = socialId;
        this.nickName = nickName;
        this.reviewScore = reviewScore;
    }

    @Builder
    public Member(RoleType roleType, MemberProvider memberProvider, String socialId, NickName nickName, Integer reviewScore) {
        this.roleType = roleType;
        this.memberProvider = memberProvider;
        this.socialId = socialId;
        this.nickName = nickName;
        this.reviewScore = reviewScore;
    }

    public String getNickName() {
        return nickName.getNickName();
    }

    public void addActivityArea(ActivityArea activityArea) {
        activityAreas.add(activityArea);
    }

}
