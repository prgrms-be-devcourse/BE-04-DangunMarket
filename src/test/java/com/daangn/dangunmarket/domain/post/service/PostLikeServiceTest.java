package com.daangn.dangunmarket.domain.post.service;

import com.daangn.dangunmarket.domain.area.model.Area;
import com.daangn.dangunmarket.domain.area.repository.AreaJpaRepository;
import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.model.PostLike;
import com.daangn.dangunmarket.domain.post.model.TradeStatus;
import com.daangn.dangunmarket.domain.post.model.vo.Price;
import com.daangn.dangunmarket.domain.post.model.vo.Title;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import com.daangn.dangunmarket.domain.post.repository.postlike.PostLikeRepository;
import com.daangn.dangunmarket.domain.post.service.dto.LikedPostFindResponse;
import com.daangn.dangunmarket.domain.post.service.dto.LikedPostFindResponseList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PostLikeServiceTest {

    @Autowired
    private PostLikeService postLikeService;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AreaJpaRepository areaJpaRepository;

    private Area setupArea;
    private Post setupPost;

    @BeforeEach
    void setup() {
        setupData();
    }

    @Test
    @DisplayName("MemberId를 통헤 좋아요한 게시물들을 찾은 다음 응답값들을 확인한다.")
    void findByMemberId_correctMemberId_LikedPostFindResponseList() {
        //given
        PageRequest requestPageAble = PageRequest.of(0, 10);

        //when
        LikedPostFindResponseList responses = postLikeService.findByMemberId(1L, requestPageAble);

        //then
        List<LikedPostFindResponse> content = responses.likedPosts().getContent();
        int size = responses.likedPosts().getContent().size();

        assertThat(size).isEqualTo(1);
        assertThat(content.get(0).title()).isEqualTo("테스트 게시글 제목");
        assertThat(content.get(0).imageUrl()).isEqualTo("abc");
        assertThat(content.get(0).areaName()).isEqualTo("서울특별시 종로구 사직동");
        assertThat(content.get(0).price()).isEqualTo(300L);
        assertThat(content.get(0).likeCount()).isEqualTo(1);
    }

    private void setupData() {
        String wkt = "MULTIPOLYGON (((30 20,45 40,10 40,30 20)), ((15 5,40 10,10 20,15 5)))";
        WKTReader reader = new WKTReader(new GeometryFactory());

        MultiPolygon multiPolygon = null;
        try {
            multiPolygon = (MultiPolygon) reader.read(wkt);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        Area area = new Area(
                "1101053",
                "서울특별시 종로구 사직동",
                multiPolygon,
                LocalDateTime.now());
        setupArea = areaJpaRepository.save(area);

        setupPost = postRepository.save(new Post(
                2L,
                setupArea.getId(),
                null,
                List.of(new PostImage("abc", "파일 이름"), new PostImage("123", "파일이름2"), new PostImage("45678", "파일이름3")),
                null,
                TradeStatus.IN_PROGRESS,
                new Title("테스트 게시글 제목"),
                "테스트 게시글 내용",
                new Price(300L),
                true,
                LocalDateTime.now(),
                1
        ));

        postLikeRepository.save(new PostLike(
                setupPost,
                1l
        ));
    }

}
