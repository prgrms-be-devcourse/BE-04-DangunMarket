package com.daangn.dangunmarket.domain.member.facade;

import com.amazonaws.services.kms.model.NotFoundException;

import com.daangn.dangunmarket.domain.DataInitializerFactory;
import com.daangn.dangunmarket.domain.area.model.Area;
import com.daangn.dangunmarket.domain.area.service.AreaService;
import com.daangn.dangunmarket.domain.member.facade.dto.ActivityAreaCreateRequestParam;
import com.daangn.dangunmarket.domain.member.facade.dto.ActivityAreaIsVerifiedRequestParam;
import com.daangn.dangunmarket.domain.member.model.*;
import com.daangn.dangunmarket.domain.member.service.ActivityAreaService;
import com.daangn.dangunmarket.domain.member.service.MemberService;
import com.daangn.dangunmarket.domain.member.service.dto.ActivityAreaFindResponse;
import com.daangn.dangunmarket.domain.member.service.dto.MemberCreateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class ActivityAreaFacadeTest {

    @Autowired
    private ActivityAreaFacade activityAreaFacade;

    @Autowired
    private ActivityAreaService activityAreaService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private MemberService memberService;

    private Member member;
    private MemberCreateResponse saveMember;
    private Long activityId;
    private ActivityAreaFindResponse findActivityArea;
    private ActivityAreaCreateRequestParam activityAreaCreateRequestParam;
    private ActivityAreaIsVerifiedRequestParam activityAreaIsVerifiedRequestParam;

    @BeforeEach
    void setUp() throws ParseException {
        member = DataInitializerFactory.member();
        saveMember = memberService.save(member);
        activityAreaCreateRequestParam = new ActivityAreaCreateRequestParam(37.575328952171, 126.96496674529);
        activityAreaIsVerifiedRequestParam = new ActivityAreaIsVerifiedRequestParam(37.575328952171, 126.96496674529);
        setUpArea();
    }

    @Test
    @DisplayName("위도, 경도를 받아 저장한 활동 지역에 회원의 정보가 제대로 저장되었음을 확인한다.")
    public void createActivityArea_returnActivityArea_equalsSavedActivityArea() {
        //when
        activityId = activityAreaFacade.createActivityArea(activityAreaCreateRequestParam, saveMember.id());
        findActivityArea = activityAreaService.findByActivityId(activityId);

        //then
        assertThat(findActivityArea.id()).isEqualTo(activityId);
        assertThat(findActivityArea.member().getId()).isEqualTo(saveMember.id());
        assertThat(findActivityArea.member().getMemberProvider()).isEqualTo(saveMember.memberProvider());
        assertThat(findActivityArea.member().getNickName()).isEqualTo(saveMember.nickName());
        assertThat(findActivityArea.member().getSocialId()).isEqualTo(saveMember.socialId());
        assertThat(findActivityArea.member().getReviewScore()).isEqualTo(saveMember.reviewScore());
    }

    @Test
    @DisplayName("기존에 저장된 활동 지역을 수정하여 새롭게 저장할 경우 회원 당 한 개의 활동 지역을 저장할 수 있음을 보장한다.")
    void countActivityAreaByMemberId_changeActivityArea_returnOneActivityAreaPerMemberId() {
        //given
        activityId = activityAreaFacade.createActivityArea(activityAreaCreateRequestParam, saveMember.id());
        findActivityArea = activityAreaService.findByActivityId(activityId);

        ActivityAreaCreateRequestParam changedRequest = new ActivityAreaCreateRequestParam(37.589115410344, 126.9807978842);
        activityAreaFacade.createActivityArea(changedRequest, saveMember.id());

        //then
        int count = activityAreaService.countActivityAreaByMemberId(saveMember.id());

        //then
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("사용자의 위치정보가 인증된 지역의 위치 정보와 동일하면 true를 반환한다.")
    void isVerifiedActivityArea_membersLocation_equal() {
        //given
        activityId = activityAreaFacade.createActivityArea(activityAreaCreateRequestParam, saveMember.id());
        findActivityArea = activityAreaService.findByActivityId(activityId);

        //then
        boolean isVerified = activityAreaFacade.isVerifiedActivityArea(activityAreaIsVerifiedRequestParam, saveMember.id());

        //then
        assertThat(isVerified).isEqualTo(true);
    }

    @Test
    @DisplayName("사용자의 위치정보가 인증된 지역의 위치 정보와 동일하지 핞으면 NOT FOUND EXCEPTION이 발생한다.")
    void isVerifiedActivityArea_notEqualsLocation_throwException() {
        //given
        activityId = activityAreaFacade.createActivityArea(activityAreaCreateRequestParam, saveMember.id());
        findActivityArea = activityAreaService.findByActivityId(activityId);
        ActivityAreaIsVerifiedRequestParam requestParam = new ActivityAreaIsVerifiedRequestParam(37.589115410344, 126.9807978842);

        //when_then
        assertThrows(NotFoundException.class, () -> {
            activityAreaFacade.isVerifiedActivityArea(requestParam, saveMember.id());
        });
    }

    void setUpArea() throws ParseException {
        List<Area> areas = DataInitializerFactory.getAreas();
        areaService.save(areas.get(0));
        areaService.save(areas.get(1));
    }

}
