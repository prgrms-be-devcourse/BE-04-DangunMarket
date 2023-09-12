package com.daangn.dangunmarket.domain.member.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Column(nullable = false)
    private String socialId;

    @Embedded
    @Column(nullable = false)
    private NickName nickName;

    @Column
    private Integer reviewScore;

    @OneToMany(mappedBy = "member")
    private List<ActivityArea> activityArea = new ArrayList<>();

    @Builder
    public Member(Long id, Long chatInformationId, Long messageId, Long productsId, Long wishListId,
                  Long activityAreasId, RoleType roleType, MemberProvider memberProvider, String socialId,
                  NickName nickName, Integer reviewScore) {
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
        this.activityArea.add(activityArea);
    }
}
