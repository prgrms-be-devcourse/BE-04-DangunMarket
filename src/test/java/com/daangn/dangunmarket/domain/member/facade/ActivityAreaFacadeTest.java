package com.daangn.dangunmarket.domain.member.facade;

import com.amazonaws.services.kms.model.NotFoundException;
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
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
        member = Member.builder()
                .roleType(RoleType.USER)
                .reviewScore(0)
                .memberProvider(MemberProvider.GOOGLE)
                .nickName(new NickName("불가사리"))
                .socialId("1234")
                .build();

        saveMember = memberService.save(member);
        activityAreaCreateRequestParam = new ActivityAreaCreateRequestParam(37.575328952171, 126.96496674529);
        activityAreaIsVerifiedRequestParam = new ActivityAreaIsVerifiedRequestParam(37.575328952171, 126.96496674529);

        setUpArea();
    }

    @Test
    @DisplayName("위도, 경도를 받아 저장한 활동 지역에 회원의 정보가 제대로 저장되었음을 확인한다.")
    void createActivityArea_returnActivityArea_equalsSavedActivityArea() {
        //when
        activityId = activityAreaFacade.createActivityArea(activityAreaCreateRequestParam,saveMember.id());
        findActivityArea = activityAreaService.findByActivityId(activityId);

        //then
        assertThat(findActivityArea.id()).isEqualTo(activityId);
        assertThat(findActivityArea.member().getId()).isEqualTo(saveMember.id());
        assertThat(findActivityArea.member().getMemberProvider()).isEqualTo(MemberProvider.GOOGLE);
        assertThat(findActivityArea.member().getNickName()).isEqualTo("불가사리");
        assertThat(findActivityArea.member().getSocialId()).isEqualTo("1234");
        assertThat(findActivityArea.member().getReviewScore()).isEqualTo(0);
    }

    @Test
    @DisplayName("기존에 저장된 활동 지역을 수정하여 새롭게 저장할 경우 회원 당 한 개의 활동 지역을 저장할 수 있음을 보장한다.")
    void countActivityAreaByMemberId_changeActivityArea_returnOneActivityAreaPerMemberId() {
        //given
        activityId = activityAreaFacade.createActivityArea(activityAreaCreateRequestParam,saveMember.id());
        findActivityArea = activityAreaService.findByActivityId(activityId);

        ActivityAreaCreateRequestParam changedRequest = new ActivityAreaCreateRequestParam(37.589115410344, 126.9807978842);
        activityAreaFacade.createActivityArea(changedRequest,saveMember.id());

        //then
        int count = activityAreaService.countActivityAreaByMemberId(saveMember.id());

        //then
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("사용자의 위치정보가 인증된 지역의 위치 정보와 동일하면 true를 반환한다.")
    void isVerifiedActivityArea_membersLocation_equal() {
        //given
        activityId = activityAreaFacade.createActivityArea(activityAreaCreateRequestParam,saveMember.id());
        findActivityArea = activityAreaService.findByActivityId(activityId);

        //then
        boolean isVerified = activityAreaFacade.isVerifiedActivityArea(activityAreaIsVerifiedRequestParam,saveMember.id());

        //then
        assertThat(isVerified).isEqualTo(true);
    }

    @Test
    @DisplayName("사용자의 위치정보가 인증된 지역의 위치 정보와 동일하지 핞으면 NOT FOUND EXCEPTION이 발생한다.")
    void isVerifiedActivityArea_notEqualsLocation_throwException() {
        //given
        activityId = activityAreaFacade.createActivityArea(activityAreaCreateRequestParam,saveMember.id());
        findActivityArea = activityAreaService.findByActivityId(activityId);
        ActivityAreaIsVerifiedRequestParam requestParam = new ActivityAreaIsVerifiedRequestParam(37.589115410344, 126.9807978842);

        //when_then
        assertThrows(NotFoundException.class, () -> {
            activityAreaFacade.isVerifiedActivityArea(requestParam, saveMember.id());
        });
    }

    void setUpArea() throws ParseException {
        String wkt1 = "MULTIPOLYGON(((126.97688884274817 37.575650779448786,126.9770344988775 37.56919453005455,126.97597472821249 37.56933629942577,126.97537470991254 37.56931556702156,126.97433193562325 37.56926180051753,126.96904837001854 37.56819441770833,126.96854493603384 37.56842767961275,126.9666499598212 37.56949165520658,126.96628175024485 37.569700734798694,126.9660973270804 37.56985650972371,126.96572852922577 37.570183936115114,126.96592699822128 37.570318805686206,126.96601094018429 37.57154839557747,126.96365922052196 37.57517466066037,126.9630860043451 37.57648592001554,126.96284099051198 37.57666158609274,126.96281041047263 37.57944880965677,126.96742431584332 37.57960153712449,126.96742176302651 37.579263521441646,126.9674300601846 37.579192577998604,126.9674570900956 37.57897525058544,126.96806604699626 37.57824678046788,126.96895511695477 37.57793526234028,126.96921284296906 37.57793529930939,126.96941453886579 37.57812112414217,126.9696644266947 37.578531136682216,126.96966721914872 37.57873620513493,126.96966877353309 37.57899287900988,126.96966949910363 37.57911252674959,126.96990457361626 37.57930175362873,126.97135197544759 37.57951327793981,126.97381925784454 37.579372140302624,126.97391736338342 37.57848707304101,126.97393961998088 37.57824042997809,126.97433153835757 37.57574990629986,126.97580378997804 37.57564946882421,126.97688884274817 37.575650779448786)))";
        String wkt2 = "MULTIPOLYGON(((126.98268938649305 37.5950655194224,126.98337258456999 37.59435192551688,126.98386809792802 37.59385046812643,126.9840115836973 37.59375538189762,126.98427349619085 37.593581837353476,126.98467876724008 37.593331231471026,126.98522535985275 37.5930571567418,126.98528680814083 37.593026356335294,126.98532872580722 37.59300572621492,126.9857592771792 37.59279401160435,126.98647984078382 37.59250646566558,126.98752622820251 37.592246800190104,126.9887608091341 37.59210400307576,126.98910945071732 37.591953415500925,126.98947579208668 37.5917950211998,126.98907504810013 37.59139446162827,126.98789849580885 37.5905567152052,126.98621002796946 37.58901596665143,126.9860138742825 37.58820164992095,126.98601087423725 37.588020241414476,126.98595696428241 37.587418359612826,126.98579012895927 37.58707512222899,126.98533710003068 37.586145890121195,126.98497436228654 37.58563328549675,126.98446533234532 37.58537368995112,126.98371421781621 37.5849704355389,126.98291838651029 37.58345832694683,126.98284931592738 37.58322799314754,126.98283620716704 37.58285406704333,126.98284210401096 37.58271776362089,126.98286481989483 37.5826063226581,126.9829917870471 37.58205705864078,126.98403469536836 37.578433706719785,126.98463962427503 37.577342343203,126.98553316382565 37.57667402546201,126.98494884418297 37.576335880365896,126.98434478868091 37.5760645251064,126.98314665239185 37.57552636292861,126.97983903605082 37.575943353839534,126.9795924891094 37.57597727455634,126.97960579128436 37.57660704191863,126.97961751615897 37.577161447995685,126.97968954034279 37.57860955110071,126.97974112529974 37.579131431874536,126.98002822496196 37.580086995769,126.9803037016248 37.580986173619735,126.97941793890371 37.58590023032471,126.9789736688563 37.587178178288674,126.9788795757727 37.58727127731593,126.97656681181762 37.58929695337157,126.97615022517256 37.589545227974334,126.97569334351333 37.58968403424009,126.97363965976345 37.59259411899389,126.97358726355343 37.593289023280704,126.97585113775686 37.59656422224409,126.98105808767329 37.59506510110589,126.98268938649305 37.5950655194224)))";

        MultiPolygon multiPolygon1 = setUpMultiPolygon(wkt1);
        MultiPolygon multiPolygon2 = setUpMultiPolygon(wkt2);

        Area area1 = Area.builder()
                .admCode("123")
                .version(LocalDateTime.now())
                .name("서울특별시 종로구 사직동")
                .geom(multiPolygon1)
                .build();

        Area area2 = Area.builder()
                .admCode("321")
                .version(LocalDateTime.now())
                .name("서울특별시 종로구 삼천동")
                .geom(multiPolygon2)
                .build();

        areaService.save(area1);
        areaService.save(area2);
    }

    private MultiPolygon setUpMultiPolygon(String wkt) throws ParseException {
        GeometryFactory factory = new GeometryFactory();

        WKTReader reader = new WKTReader(factory);
        MultiPolygon multiPolygon = (MultiPolygon) reader.read(wkt);

        return multiPolygon;
    }

}
