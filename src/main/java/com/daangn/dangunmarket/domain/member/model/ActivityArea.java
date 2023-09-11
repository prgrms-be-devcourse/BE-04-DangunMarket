package com.daangn.dangunmarket.domain.member.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.Assert;

@Entity
@Table(name="activity_areas")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Long emdAreaId;

    @Column
    private boolean isVerified;

    public ActivityArea(Long id, Member member, Long emdAreaId, boolean isVerified) {
        this.id = id;
        this.member = member;
        this.emdAreaId = emdAreaId;
        this.isVerified = isVerified;
    }

    public void changeAreaId(Long emdAreaId) {
        Assert.notNull(emdAreaId,"활동 지역 아이디는 null일 수 없습니다.");
        this.emdAreaId = emdAreaId;
    }

    public void addMember(Member member) {
        this.member = member;
        member.addActivityArea(this);
    }

}
