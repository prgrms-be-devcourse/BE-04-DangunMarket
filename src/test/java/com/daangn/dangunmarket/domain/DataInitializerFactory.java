package com.daangn.dangunmarket.domain;

import com.daangn.dangunmarket.domain.area.model.Area;
import com.daangn.dangunmarket.domain.chat.model.ChatRoom;
import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.model.NickName;
import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostUpdateRequestParam;
import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.model.TradeStatus;
import com.daangn.dangunmarket.domain.post.model.vo.Price;
import com.daangn.dangunmarket.domain.post.model.vo.Title;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.daangn.dangunmarket.domain.member.model.MemberProvider.GOOGLE;
import static com.daangn.dangunmarket.domain.member.model.RoleType.USER;

public final class DataInitializerFactory {

    public static List<Area> getAreas() throws ParseException {
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

        return List.of(area1, area2);
    }

    private static MultiPolygon setUpMultiPolygon(String wkt) throws ParseException {
        GeometryFactory factory = new GeometryFactory();

        WKTReader reader = new WKTReader(factory);
        MultiPolygon multiPolygon = (MultiPolygon) reader.read(wkt);

        return multiPolygon;
    }

    public static Member member() {
        return Member.builder()
                .roleType(USER)
                .memberProvider(GOOGLE)
                .socialId("member2 socialId")
                .nickName(new NickName("james"))
                .reviewScore(35)
                .build();
    }

    public static Member member2() {
        return Member.builder()
                .roleType(USER)
                .memberProvider(GOOGLE)
                .socialId("member2 socialId")
                .nickName(new NickName("byeol"))
                .reviewScore(26)
                .build();
    }

    public static Member member(String nickName, Integer reviewScore) {
        return Member.builder()
                .roleType(USER)
                .memberProvider(GOOGLE)
                .socialId("member2 socialId")
                .nickName(new NickName(nickName))
                .reviewScore(reviewScore)
                .build();
    }

    public static Category category() {
        return new Category("전자기기", null, 1L, new ArrayList<>());
    }

    public static List<PostCreateRequestParam> getPostCreateRequestParams(Long memberId, Long categoryId) {
        List<MultipartFile> mockMultipartFiles1 = List.of(new MockMultipartFile("third", (byte[]) null), new MockMultipartFile("four", (byte[]) null));
        PostCreateRequestParam requestParam1 = new PostCreateRequestParam(memberId, 1L, 53.5297, 126.8876, "네이버 그린 팩토리", mockMultipartFiles1, categoryId, "SetupTile", "SetupContent", 200L, true, LocalDateTime.now());

        List<MultipartFile> mockMultipartFiles2 = List.of(new MockMultipartFile("first", (byte[]) null), new MockMultipartFile("second", (byte[]) null));
        PostCreateRequestParam requestParam2 = new PostCreateRequestParam(memberId, 2L, 37.5297, 126.8876, "데브코스 공원 비둘기 벤치", mockMultipartFiles2, categoryId, "firstTile", "firstContent", 100L, true, LocalDateTime.now());

        return List.of(requestParam1, requestParam2);
    }

    public static PostUpdateRequestParam postUpdateRequestParam(Long postId, Long categoryId) {
        List<MultipartFile> updateMockMultipartFiles = List.of(new MockMultipartFile("테스트1", (byte[]) null), new MockMultipartFile("테스트2", (byte[]) null));
        List<String> urls = new ArrayList<>();
        return new PostUpdateRequestParam(postId, 37.5297, 126.8876, "데브코스 공원 벤치", updateMockMultipartFiles, urls, categoryId, "의자 팔아요", "아기가 쓰던 의자입니다.", 100000L, true);
    }

    public static ChatRoomInfo sellerChatRoomInfo(Long postId, Long writerId, ChatRoom chatRoom) {
        return new ChatRoomInfo(true, postId, chatRoom, writerId);
    }

    public static ChatRoomInfo buyerChatRoomInfo(Long postId, Long buyerId, ChatRoom chatRoom) {
        return new ChatRoomInfo(false, postId, chatRoom, buyerId);
    }

    public static Post post(Long memberId, Category category) {
        return new Post(memberId, 2L, null, new ArrayList<>(), category, TradeStatus.IN_PROGRESS, new Title("달님이 젤리 가게"), "사용감 있습니다.", new Price(10000), true, LocalDateTime.now(), 0);
    }

}
