package com.daangn.dangunmarket.domain.post.postlike;

import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.model.NickName;
import com.daangn.dangunmarket.domain.member.repository.MemberJpaRepository;
import com.daangn.dangunmarket.domain.post.facade.PostFacade;
import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.Post;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.daangn.dangunmarket.domain.member.model.MemberProvider.GOOGLE;
import static com.daangn.dangunmarket.domain.member.model.RoleType.USER;

@SpringBootTest
@ActiveProfiles("test")
public class PostLikeServiceTest {

    @Autowired
    private PostLikeService postLikeService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostFacade postFacade;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private PostRepository postRepository;

    private Long memberId1;
    private Long memberId2;
    private Long postId;

    @BeforeEach
    void setUp() {
        setUpData();
    }

    @Test
    @DisplayName("좋아요 기능 동시성 테스트")
    void likePost_PostIdMemberId_Success() throws InterruptedException {
        //given
        final int THREAD_NUM = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUM);

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
        postLikeService.likePost(memberId1, postId);

        //when & then
        Assertions.assertThatThrownBy(() -> postLikeService.likePost(memberId1, postId))
                .isInstanceOf(InvalidPostLikeException.class);
    }

    private void setUpData() {
        Member member1 = Member.builder()
                .id(1L)
                .roleType(USER)
                .memberProvider(GOOGLE)
                .socialId("member1 socialId")
                .nickName(new NickName("nickname1"))
                .reviewScore(34)
                .build();
        Member member2 = Member.builder()
                .id(2L)
                .roleType(USER)
                .memberProvider(GOOGLE)
                .socialId("member2 socialId")
                .nickName(new NickName("nickname2"))
                .reviewScore(35)
                .build();
        memberId1 = memberJpaRepository.save(member1).getId();
        memberId2 = memberJpaRepository.save(member2).getId();

        Category category1 = categoryRepository.save(new Category("중고서적", null, 1L, null));

        List<MultipartFile> multiPartFiles = new ArrayList<>();
        PostCreateRequestParam requestParam = new PostCreateRequestParam(
                2L,
                1L,
                53.5297,
                126.8876,
                "네이버 그린 팩토리",
                multiPartFiles,
                category1.getId(),
                "테스트 title",
                "테스트 Content",
                200L,
                true,
                LocalDateTime.now());
        postId = postFacade.createPost(requestParam);
    }

}
