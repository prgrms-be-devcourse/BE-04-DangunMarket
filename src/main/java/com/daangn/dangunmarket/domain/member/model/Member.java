package com.daangn.dangunmarket.domain.member.model;

import com.daangn.dangunmarket.domain.member.model.MemberProvider;
import com.daangn.dangunmarket.domain.member.model.NickName;
import com.daangn.dangunmarket.domain.member.model.RoleType;
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

    @Column
    private Long chatInformationId;

    @Column
    private Long messageId;

    @Column
    private Long productsId;

    @Column
    private Long wishListId;

    // 직접참조
    @Column
    private Long activityAreasId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType roleType;

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
