package com.daangn.dangunmarket.domain.post.service;

import com.daangn.dangunmarket.domain.post.exception.TooEarlyToRefreshException;
import com.daangn.dangunmarket.domain.post.model.LocationPreference;
import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.model.TradeStatus;
import com.daangn.dangunmarket.domain.post.model.vo.Price;
import com.daangn.dangunmarket.domain.post.model.vo.Title;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import com.daangn.dangunmarket.domain.post.service.dto.PostUpdateStatusRequest;
import com.daangn.dangunmarket.domain.post.service.mapper.PostDtoMapper;
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
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PostServiceTest {

    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostDtoMapper postDtoMapper;

    @Autowired
    private PostMapper mapper;

    @MockBean
    private TimeGenerator timeGenerator;

    private Post setupPost1;
    private Post setupPost2;

    @BeforeEach
    void setup() {
        postService = new PostService(postRepository, postDtoMapper, mapper, timeGenerator);

        setupData();
    }

    @Test
    @DisplayName("post의 Status를 변경할 수 있다.")
    void changeStatus_correctRequest_PostId() {
        //given
        Long postId = setupPost1.getId();
        PostUpdateStatusRequest request = new PostUpdateStatusRequest(RESERVATED, postId);

        //when
        Long responsePostId = postService.changeStatus(request);

        //then
        Post post = postRepository.findById(responsePostId).get();
        assertThat(post.getTradeStatus()).isEqualTo(RESERVATED);
    }

    @Test
    @DisplayName("post의 refreshAt의 시간을 현재 시간으로 업데이트 할 수 있다.")
    void refreshTime_refreshablePostId_PostId() {
        //given
        LocalDateTime now = LocalDateTime.now();
        given(timeGenerator.getCurrentTime()).willReturn(now);

        //when
        Long postId = postService.refreshTime(setupPost1.getId(), 1L);

        //then
        Post post = postRepository.findById(postId).get();
        assertThat(post.getRefreshedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("2일 이내에 Post를 refresh할 경우 예외가 발생하며 예외에는 refresh 가능할 때 까지 남은 기간이 담겨있다.")
    void refreshTime_unRefreshablePost_TooEarlyToRefreshException(){
        //given
        LocalDateTime now = LocalDateTime.of(2023, 9, 10, 10, 20);
        given(timeGenerator.getCurrentTime()).willReturn(now);

        //when
        TooEarlyToRefreshException exception = (TooEarlyToRefreshException) catchException(() ->
                postService.refreshTime(setupPost2.getId(), 1L));

        //then
        assertThat(exception).isInstanceOf(TooEarlyToRefreshException.class);
        assertThat(exception.getRemainingDays()).isEqualTo(1);
        assertThat(exception.getRemainingHours()).isEqualTo(13);
        assertThat(exception.getRemainingMinutes()).isEqualTo(40);
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

        setupPost1 = postRepository.save(post);

        Point point2 = GeometryTypeFactory.createPoint(39.3, 75.7);
        Post post2 = new Post(1L,
                2L,
                new LocationPreference(point2, "테스트 위치2"),
                new ArrayList<>(),
                null,
                TradeStatus.IN_PROGRESS,
                new Title("테스트 글 제목2"),
                "테스트 글 내용2",
                new Price(790L),
                true,
                LocalDateTime.of(2023, 9, 10, 0, 0),
                1);

        setupPost2 = postRepository.save(post2);
    }

}