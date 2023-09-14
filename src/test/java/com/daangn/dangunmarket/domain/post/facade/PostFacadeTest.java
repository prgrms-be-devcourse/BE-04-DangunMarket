package com.daangn.dangunmarket.domain.post.facade;

import com.daangn.dangunmarket.domain.area.service.AreaService;
import com.daangn.dangunmarket.domain.area.service.dto.AreaResponse;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.model.NickName;
import com.daangn.dangunmarket.domain.member.repository.MemberJpaRepository;
import com.daangn.dangunmarket.domain.member.service.MemberService;
import com.daangn.dangunmarket.domain.post.exception.UnauthorizedAccessException;
import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostFindResponseParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostToUpdateResponseParam;
import com.daangn.dangunmarket.domain.post.facade.mpper.PostParamDtoMapper;
import com.daangn.dangunmarket.domain.post.facade.mpper.PostParamMapper;
import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.repository.category.CategoryRepository;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import com.daangn.dangunmarket.domain.post.service.CategoryService;
import com.daangn.dangunmarket.domain.post.service.PostService;
import com.daangn.dangunmarket.global.aws.dto.ImageInfo;
import com.daangn.dangunmarket.global.aws.s3.S3Uploader;
import com.daangn.dangunmarket.global.exception.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.daangn.dangunmarket.domain.member.model.MemberProvider.GOOGLE;
import static com.daangn.dangunmarket.domain.member.model.RoleType.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PostFacadeTest {

    private PostFacade postFacade;

    @Autowired
    private PostService postService;

    @Autowired
    private MemberService memberService;

    @MockBean
    private AreaService areaService;

    @Autowired
    private CategoryService categoryService;

    @MockBean
    private S3Uploader s3Uploader;

    @Autowired
    private PostParamMapper mapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostParamDtoMapper postParamDtoMapper;

    private Long setupProductId;

    private Category setupCategory;

    private Long setupMemberId;

    @BeforeEach
    void setup() {
        postFacade = new PostFacade(
                postService,
                memberService,
                areaService,
                categoryService,
                s3Uploader,
                mapper, postParamDtoMapper);

        given(s3Uploader.saveImages(any())).willReturn(List.of(new ImageInfo("z", "zname"), new ImageInfo("s", "sname")));

        dataSetup();
    }

    @Test
    @DisplayName("product를 저장 후 저장된 product불러와 필드값들을 확인한다.")
    void createProduct_correctRequest_Long() {
        //given
        given(s3Uploader.saveImages(any())).willReturn(List.of(new ImageInfo("a", "zname"), new ImageInfo("b", "sname"), new ImageInfo("c", "sname")));
        List<MultipartFile> mockMultipartFiles = List.of(new MockMultipartFile("first", (byte[]) null), new MockMultipartFile("second", (byte[]) null));
        PostCreateRequestParam requestParam = new PostCreateRequestParam(setupMemberId, 2L, 37.5297, 126.8876, "seoul", mockMultipartFiles, setupCategory.getId(), "firstTile", "firstContent", 100L, true, LocalDateTime.now());

        //when
        Long productId = postFacade.createPost(requestParam);

        //then
        Post post = postRepository.findById(productId).orElseThrow();
        assertThat(post.getMemberId()).isEqualTo(setupMemberId);
        assertThat(post.getAreaId()).isEqualTo(2l);
        assertThat(post.getLocalPreference().getAlias()).isEqualTo("seoul");
        assertThat(post.getPostImageList().size()).isEqualTo(3);
        assertThat(post.getTitle()).isEqualTo("firstTile");
        assertThat(post.getContent()).isEqualTo("firstContent");
        assertThat(post.getPrice()).isEqualTo(100L);
    }

    @Test
    @DisplayName("setup된 Product의 ID를 통해 조회하여 응답값을 검사한다.")
    void findById_correctProductId_ProductFindResponseParam() {
        //given
        AreaResponse setupAreaResponse = new AreaResponse(1L, "12345", "서울시 강남구 청담동", LocalDateTime.now());
        given(areaService.findById(any())).willReturn(setupAreaResponse);

        //when
        PostFindResponseParam responseParam = postFacade.findById(setupProductId);

        //then
        assertThat(responseParam.areaName()).isEqualTo("서울시 강남구 청담동");
        assertThat(responseParam.price()).isEqualTo(200L);
        assertThat(responseParam.title()).isEqualTo("SetupTile");
        assertThat(responseParam.content()).isEqualTo("SetupContent");
        assertThat(responseParam.categoryName()).isEqualTo("전자기기");
        assertThat(responseParam.latitude()).isEqualTo(53.5297);
        assertThat(responseParam.longitude()).isEqualTo(126.8876);
        assertThat(responseParam.memberName()).isEqualTo("james");
        assertThat(responseParam.urls().size()).isEqualTo(2);
        assertThat(responseParam.likeCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글을 수정하기 위해서 기존에 작성한 글에 대한 정보를 제대로 불러오는지 확인한다.")
    void findPostInfoToUpdate_createdPost_equals() {
        //given
        given(s3Uploader.saveImages(any())).willReturn(List.of(new ImageInfo("a", "zname"), new ImageInfo("b", "sname"), new ImageInfo("c", "sname")));
        List<MultipartFile> mockMultipartFiles = List.of(new MockMultipartFile("first", (byte[]) null), new MockMultipartFile("second", (byte[]) null));
        PostCreateRequestParam requestParam = new PostCreateRequestParam(1L, 2L, 37.5297, 126.8876, "seoul", mockMultipartFiles, setupCategory.getId(), "firstTile", "firstContent", 100L, true, LocalDateTime.now());

        Long productId = postFacade.createPost(requestParam);

        //when
        PostToUpdateResponseParam postInfoToUpdate = postFacade.findPostInfoToUpdateById(1L, productId);

        //then
        assertThat(postInfoToUpdate.postImages().size()).isEqualTo(3);
        assertThat(postInfoToUpdate.locationPreferenceAlias()).isEqualTo("seoul");
        assertThat(postInfoToUpdate.title()).isEqualTo("firstTile");
        assertThat(postInfoToUpdate.content()).isEqualTo("firstContent");
        assertThat(postInfoToUpdate.price()).isEqualTo(100L);
    }

    @Test
    @DisplayName("게시글 수정을 요청한 회원과 게시글 작성자와 다른 경우 예외를 발생시킨다.")
    void findPostInfoToUpdate_notWriter_throwException() {
        //given
        given(s3Uploader.saveImages(any())).willReturn(List.of(new ImageInfo("a", "zname"), new ImageInfo("b", "sname"), new ImageInfo("c", "sname")));
        List<MultipartFile> mockMultipartFiles = List.of(new MockMultipartFile("first", (byte[]) null), new MockMultipartFile("second", (byte[]) null));
        PostCreateRequestParam requestParam = new PostCreateRequestParam(1L, 2L, 37.5297, 126.8876, "seoul", mockMultipartFiles, setupCategory.getId(), "firstTile", "firstContent", 100L, true, LocalDateTime.now());

        Long productId = postFacade.createPost(requestParam);

        //when_then
        assertThrows(UnauthorizedAccessException.class, () -> {
            postFacade.findPostInfoToUpdateById(2L, productId);
        });
    }

    @Test
    @DisplayName("게시글을 작성한 유저가 게시글 삭제 요청 시 게시글을 성공적으로 삭제한다.")
    void deletePost_MemberIdAndPostID_Success() {
        //given
        Long memberId = 1L;
        given(s3Uploader.saveImages(any())).willReturn(List.of(new ImageInfo("a", "zname"), new ImageInfo("b", "sname"), new ImageInfo("c", "sname")));
        List<MultipartFile> mockMultipartFiles = List.of(new MockMultipartFile("first", (byte[]) null), new MockMultipartFile("second", (byte[]) null));
        PostCreateRequestParam requestParam = new PostCreateRequestParam(memberId, 2L, 37.5297, 126.8876, "seoul", mockMultipartFiles, setupCategory.getId(), "firstTile", "firstContent", 100L, true, LocalDateTime.now());
        Long postId = postFacade.createPost(requestParam);

        //when
        Long deletePostId = postFacade.deletePost(memberId, postId);

        //then
        Assertions.assertThatThrownBy(() -> postFacade.findById(deletePostId))
                .isInstanceOf(EntityNotFoundException.class);

    }


    @Test
    @DisplayName("게시글을 삭제 권한이 없는 유저가 게시글 삭제 요청시 예외가 발생한다. ")
    void deletePost_MemberIdAndPostID_Throw() {
        //given
        Long memberId = 1L;
        given(s3Uploader.saveImages(any())).willReturn(List.of(new ImageInfo("a", "zname"), new ImageInfo("b", "sname"), new ImageInfo("c", "sname")));
        List<MultipartFile> mockMultipartFiles = List.of(new MockMultipartFile("first", (byte[]) null), new MockMultipartFile("second", (byte[]) null));
        PostCreateRequestParam requestParam = new PostCreateRequestParam(memberId, 2L, 37.5297, 126.8876, "seoul", mockMultipartFiles, setupCategory.getId(), "firstTile", "firstContent", 100L, true, LocalDateTime.now());
        Long postId = postFacade.createPost(requestParam);

        //when
        Long strangeMemberId = 5L;
        Assertions.assertThatThrownBy(() -> postFacade.deletePost(strangeMemberId, postId))
                .isInstanceOf(UnauthorizedAccessException.class);
    }


    /**
     * Member, Category, Product을 미리 setup함.
     */
    private void dataSetup() {
        Member setupMember = Member.builder()
                .roleType(USER)
                .memberProvider(GOOGLE)
                .socialId("member2 socialId")
                .nickName(new NickName("james"))
                .reviewScore(35)
                .build();
        setupMemberId = memberJpaRepository.save(setupMember).getId();

        Category setupCategory = new Category("전자기기", null, 1L, new ArrayList<>());
        this.setupCategory = categoryRepository.save(setupCategory);

        List<MultipartFile> mockMultipartFiles = List.of(new MockMultipartFile("third", (byte[]) null), new MockMultipartFile("four", (byte[]) null));
        PostCreateRequestParam requestParam = new PostCreateRequestParam(setupMemberId, 1L, 53.5297, 126.8876, "네이버 그린 팩토리", mockMultipartFiles, this.setupCategory.getId(), "SetupTile", "SetupContent", 200L, true, LocalDateTime.now());
        setupProductId = postFacade.createPost(requestParam);
    }

}
