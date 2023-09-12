package com.daangn.dangunmarket.domain.member.facade;

import com.daangn.dangunmarket.domain.area.service.AreaService;
import com.daangn.dangunmarket.domain.member.facade.dto.ActivityAreaCreateRequestParam;
import com.daangn.dangunmarket.domain.member.service.ActivityAreaService;
import com.daangn.dangunmarket.domain.member.service.MemberService;
import com.daangn.dangunmarket.domain.member.service.dto.MemberFindResponse;
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

        GeometryFactory factory = new GeometryFactory();
        Point point = factory.createPoint(new Coordinate(request.longitude(), request.latitude()));
        Long areaId = areaService.findAreaIdByPolygon(point);

        return activityAreaService.createActivityArea(memberFindResponse, areaId);
    }

    public boolean isVerifiedActivityArea(Double longitude, Double latitude, Long memberId) {
        GeometryFactory factory = new GeometryFactory();
        Point point = factory.createPoint(new Coordinate(longitude, latitude));
        Long areaId = areaService.findAreaIdByPolygon(point);

        return activityAreaService.isVerifiedActivityArea(memberId, areaId);
    }

}
