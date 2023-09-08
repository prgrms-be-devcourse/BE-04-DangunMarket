package com.daangn.dangunmarket.domain.member.model;

import com.daangn.dangunmarket.domain.auth.jwt.CustomUser;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Getter
@Table(name = "Members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member{

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

    @Column
    private Long activityAreasId;

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

    @Builder
    public Member(Long id, Long chatInformationId, Long messageId, Long productsId, Long wishListId,
                  Long activityAreasId, RoleType roleType, MemberProvider memberProvider, String socialId,
                  NickName nickName, Integer reviewScore) {
        this.id = id;
        this.chatInformationId = chatInformationId;
        this.messageId = messageId;
        this.productsId = productsId;
        this.wishListId = wishListId;
        this.activityAreasId = activityAreasId;
        this.roleType = roleType;
        this.memberProvider = memberProvider;
        this.socialId = socialId;
        this.nickName = nickName;
        this.reviewScore = reviewScore;
    }

}
