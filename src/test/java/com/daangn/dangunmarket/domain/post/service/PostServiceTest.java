package com.daangn.dangunmarket.domain.post.service;

import com.daangn.dangunmarket.domain.post.model.LocationPreference;
import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.model.TradeStatus;
import com.daangn.dangunmarket.domain.post.model.vo.Price;
import com.daangn.dangunmarket.domain.post.model.vo.Title;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import com.daangn.dangunmarket.domain.post.service.dto.PostUpdateStatusRequest;
import com.daangn.dangunmarket.domain.post.service.mapper.PostMapper;
import com.daangn.dangunmarket.global.GeometryTypeFactory;
import com.daangn.dangunmarket.global.TimeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.daangn.dangunmarket.domain.post.model.TradeStatus.RESERVATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PostServiceTest {

    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostMapper mapper;

    @MockBean
    private TimeGenerator timeGenerator;

    private Post setupPost;

    @BeforeEach
    void setup() {
        postService = new PostService(postRepository, mapper, timeGenerator);

        setupData();
    }

    @Test
    @DisplayName("미리 저장해놓은 post의 status를 변경하고 조회하여 status가 올바르게 변경됐는지 확인한다.")
    void changeStatus_correctRequest_PostId() {
        //given
        Long postId = setupPost.getId();
        PostUpdateStatusRequest request = new PostUpdateStatusRequest(RESERVATED, postId);

        //when
        Long responsePostId = postService.changeStatus(request);

        //then
        Post post = postRepository.findById(responsePostId).get();
        assertThat(post.getTradeStatus()).isEqualTo(RESERVATED);
    }

    @Test
    @DisplayName("셋팅해 놓은 post의 refreshAt 값을 현재 시간으로 업데이트하고 해당 post의 refreshAt을 확인한다.")
    void refreshTime_correctPostId_PostId() {
        //when
        LocalDateTime now = LocalDateTime.now();
        given(timeGenerator.getCurrentTime()).willReturn(now);

        //when
        Long postId = postService.refreshTime(setupPost.getId());

        //then
        Post post = postRepository.findById(postId).get();
        assertThat(post.getRefreshedAt()).isEqualTo(now);
    }

    /**
     * 게시글 하나를 TradeStatus.IN_PROGRESS 상태로 넣어 놓음.
     */
    private void setupData() {
        Point point = GeometryTypeFactory.createPoint(37.3, 78.7);
        Post post = new Post(1L,
                2L,
                new LocationPreference(point, "테스트 위치"),
                new ArrayList<>(),
                null,
                TradeStatus.IN_PROGRESS,
                new Title("테스트 글 제목"),
                "테스트 글 내용",
                new Price(780L),
                true,
                LocalDateTime.of(2023, 9, 10, 0, 0),
                1);

        setupPost = postRepository.save(post);
    }

}