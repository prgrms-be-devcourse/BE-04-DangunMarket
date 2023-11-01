package com.daangn.dangunmarket.domain.member.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Table(name = "members")
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

    public String getNickName() {
        return nickName.getNickName();
    }

    public void addActivityArea(ActivityArea activityArea) {
        activityAreas.add(activityArea);
    }

    public boolean isNotMemberId(Long memberId){
        return !Objects.equals(id, memberId);
    }

}
