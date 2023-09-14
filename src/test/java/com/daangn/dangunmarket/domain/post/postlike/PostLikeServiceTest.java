package com.daangn.dangunmarket.domain.post.postlike;

import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.model.NickName;
import com.daangn.dangunmarket.domain.member.repository.MemberJpaRepository;
import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.model.TradeStatus;
import com.daangn.dangunmarket.domain.post.model.vo.Price;
import com.daangn.dangunmarket.domain.post.model.vo.Title;
import com.daangn.dangunmarket.domain.post.repository.category.CategoryRepository;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import com.daangn.dangunmarket.domain.post.service.PostLikeService;
import com.daangn.dangunmarket.global.exception.EntityNotFoundException;
import com.daangn.dangunmarket.global.exception.InvalidPostLikeException;
import com.daangn.dangunmarket.global.response.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.daangn.dangunmarket.domain.member.model.MemberProvider.GOOGLE;
import static com.daangn.dangunmarket.domain.member.model.RoleType.USER;

@SpringBootTest
//@Transactional
@ActiveProfiles("test")
public class PostLikeServiceTest {

    @Autowired
    private PostLikeService postLikeService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private PostRepository postRepository;

    private Member member1;
    private Member member2;
    private Post post;

    @BeforeEach
    void setUp() {
        setUpData();
    }

    @Test
//    @Disabled
    @DisplayName("좋아요 기능 동시성 테스트")
    void likePost_PostIdMemberId_Success() throws InterruptedException {
        //given
        final int THREAD_NUM = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUM);
        Long memberId1 = memberJpaRepository.save(member1).getId();
        Long memberId2 = memberJpaRepository.save(member2).getId();
        Long postId = postRepository.save(post).getId();

        //when
        executorService.execute(() -> {
            postLikeService.likePost(memberId1, postId);
        });
        executorService.execute(() -> {
            postLikeService.likePost(memberId2, postId);
        });
        Thread.sleep(500);

        //then
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_POST_ENTITY));

        Assertions.assertThat(findPost.getLikeCount()).isEqualTo(1);
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
        member1 = Member.builder()
                .id(50L)
                .roleType(USER)
                .memberProvider(GOOGLE)
                .socialId("member1 socialId")
                .nickName(new NickName("nickname1"))
                .reviewScore(34)
                .build();
        member2 = Member.builder()
                .id(51L)
                .roleType(USER)
                .memberProvider(GOOGLE)
                .socialId("member2 socialId")
                .nickName(new NickName("nickname2"))
                .reviewScore(35)
                .build();

        Category category1 = categoryRepository.save(new Category("중고서적", null, 1L, null));

        post = Post.builder()
                .memberId(2L)
                .areaId(1L)
                .localPreference(null)
                .postImageList(List.of(new PostImage("abc", "파일이름1"), new PostImage("123", "파일이름2")))
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
