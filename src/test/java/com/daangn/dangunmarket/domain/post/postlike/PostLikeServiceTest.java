package com.daangn.dangunmarket.domain.post.postlike;

import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.model.NickName;
import com.daangn.dangunmarket.domain.member.repository.MemberJpaRepository;
import com.daangn.dangunmarket.domain.post.exception.InvalidPostLikeException;
import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.LocationPreference;
import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.model.TradeStatus;
import com.daangn.dangunmarket.domain.post.model.vo.Price;
import com.daangn.dangunmarket.domain.post.model.vo.Title;
import com.daangn.dangunmarket.domain.post.repository.category.CategoryRepository;
import com.daangn.dangunmarket.domain.post.repository.post.PostJpaRepository;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import com.daangn.dangunmarket.domain.post.repository.postlike.PostLikeJpaRepository;
import com.daangn.dangunmarket.domain.post.service.PostLikeService;
import com.daangn.dangunmarket.global.GeometryTypeFactory;
import com.daangn.dangunmarket.global.exception.EntityNotFoundException;
import com.daangn.dangunmarket.global.response.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.daangn.dangunmarket.domain.member.model.MemberProvider.GOOGLE;
import static com.daangn.dangunmarket.domain.member.model.RoleType.USER;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class PostLikeServiceTest {

    @Autowired
    private PostLikeService postLikeService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostJpaRepository postJpaRepository;

    @Autowired
    private PostLikeJpaRepository postLikeJpaRepository;

    private Member member1;
    private Post post;
    private List<Member> members;

    @BeforeEach
    void setUp() {
        setUpData();
    }

    @AfterEach
    void tearDown(){
        postLikeJpaRepository.deleteAll();
        postJpaRepository.deleteAll();
    }

    private static final int MEMBER_COUNT = 20;

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @DisplayName("좋아요를 누른 수 만큼 게시물의 좋아요 수가 증가하고, 좋아요 수랑 게시물의 좋아요 수랑 일치한다")
    void likePost_PostIdMemberId_Success() throws InterruptedException {
        //given
        final int THREAD_NUM = MEMBER_COUNT;
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUM);
        CountDownLatch latch = new CountDownLatch(THREAD_NUM);
        Long postId = postRepository.save(post).getId();

        //when
        for (Member member : this.members) {
            executorService.execute(() -> {
                try {
                    postLikeService.likePost(member.getId(), postId);
                } catch (Exception e) {
                    log.info("예외 {}, 실패한 회원 id {}, {}", e.getClass().getName(), member.getId(), e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        //then
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_POST_ENTITY));

        Assertions.assertThat(findPost.getLikeCount()).isEqualTo(MEMBER_COUNT);
    }

    @Test
    @Transactional
    @DisplayName("게시물에 좋아요를 누른 기록이 있다면 반복하여 누를 수 없다.")
    void likePost_PostIdMemberId_ThrowInvalidPostLikeException() {
        //given
        Long memberId1 = memberJpaRepository.save(member1).getId();
        Long postId = postRepository.save(post).getId();
        postLikeService.likePost(memberId1, postId);

        //when & then
        Assertions.assertThatThrownBy(() -> postLikeService.likePost(memberId1, postId))
                .isInstanceOf(InvalidPostLikeException.class);
    }

    private void setUpData() {
        this.members = IntStream.range(0, MEMBER_COUNT).mapToObj(
                it -> {
                    return memberJpaRepository.save(Member.builder()
                            .memberProvider(GOOGLE)
                            .roleType(USER)
                            .socialId("member1 socialId")
                            .nickName(new NickName("nickname1"))
                            .reviewScore(34)
                            .build());
                }
        ).collect(Collectors.toList());

        member1 = Member.builder()
                .id(1L)
                .roleType(USER)
                .memberProvider(GOOGLE)
                .socialId("member1 socialId")
                .nickName(new NickName("nickname1"))
                .reviewScore(34)
                .build();

        Category category1 = categoryRepository.save(new Category("중고서적", null, 1L, null));

        Point point = GeometryTypeFactory.createPoint(35.85, 15.95);
        post = Post.builder()
                .memberId(2L)
                .areaId(1L)
                .postImages(List.of())
                .locationPreference(new LocationPreference(point, "test alias"))
                .category(category1)
                .tradeStatus(TradeStatus.IN_PROGRESS)
                .title(new Title("제목"))
                .content("게시글")
                .price(new Price(200L))
                .isOfferAllowed(true)
                .refreshedAt(LocalDateTime.now())
                .likeCount(0)
                .build();
    }

}
