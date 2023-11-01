package com.daangn.dangunmarket.domain.member.facade;

import com.daangn.dangunmarket.domain.area.service.AreaService;
import com.daangn.dangunmarket.domain.member.facade.dto.ActivityAreaCreateRequestParam;
import com.daangn.dangunmarket.domain.member.facade.dto.ActivityAreaIsVerifiedRequestParam;
import com.daangn.dangunmarket.domain.member.service.ActivityAreaService;
import com.daangn.dangunmarket.domain.member.service.MemberService;
import com.daangn.dangunmarket.domain.member.service.dto.MemberFindResponse;
import com.daangn.dangunmarket.global.GeometryTypeFactory;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ActivityAreaFacade {

    private final ActivityAreaService activityAreaService;
    private final AreaService areaService;
    private final MemberService memberService;

    public ActivityAreaFacade(ActivityAreaService activityAreaService, AreaService areaService,
                              MemberService memberService) {
        this.activityAreaService = activityAreaService;
        this.areaService = areaService;
        this.memberService = memberService;
    }

    public Long createActivityArea(ActivityAreaCreateRequestParam request, Long memberId) {
        MemberFindResponse memberFindResponse = memberService.findById(memberId);

        Point point = GeometryTypeFactory.createPoint(request.longitude(), request.latitude());
        Long areaId = areaService.findAreaIdByPolygon(point);

        return activityAreaService.createActivityArea(memberFindResponse, areaId);
    }

    public boolean isVerifiedActivityArea(ActivityAreaIsVerifiedRequestParam requestParam, Long memberId) {
        Point point = GeometryTypeFactory.createPoint(requestParam.longitude(), requestParam.latitude());
        Long areaId = areaService.findAreaIdByPolygon(point);

        return activityAreaService.isVerifiedActivityArea(memberId, areaId);
    }

}
