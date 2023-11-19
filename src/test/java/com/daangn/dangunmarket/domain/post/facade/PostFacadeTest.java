package com.daangn.dangunmarket.domain.post.facade;

import com.amazonaws.services.kms.model.NotFoundException;
import com.daangn.dangunmarket.domain.DataInitializerFactory;
import com.daangn.dangunmarket.domain.area.service.AreaService;
import com.daangn.dangunmarket.domain.area.service.dto.AreaResponse;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.service.MemberService;
import com.daangn.dangunmarket.domain.post.exception.NotWriterException;
import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostFindResponseParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostToUpdateResponseParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostUpdateRequestParam;
import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.repository.category.CategoryRepository;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PostFacadeTest {

    @Autowired
    private PostFacade postFacade;

    @Autowired
    private MemberService memberService;

    @MockBean
    private AreaService areaService;

    @MockBean
    private S3Uploader s3Uploader;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category setUpCategory;

    private Long setupProductId;

    private Long setUpMemberId;

    private Long setupMemberId;

    @BeforeEach
    void setup() {
        given(s3Uploader.saveImages(any())).willReturn(List.of(new ImageInfo("z", "zname"), new ImageInfo("s", "sname")));
        dataSetup();
    }

    @Test
    @DisplayName("product를 저장 후 저장된 product 불러와 필드값들을 확인한다.")
    void createProduct_correctRequest_Long() {
        //given
        PostCreateRequestParam postCreateRequestParam = DataInitializerFactory.getPostCreateRequestParams(setupProductId, setUpCategory.getId()).get(1);

        //when
        Long productId = postFacade.createPost(postCreateRequestParam);

        //then
        Post post = postRepository.findById(productId).orElseThrow();

        assertThat(post.getMemberId()).isEqualTo(postCreateRequestParam.memberId());
        assertThat(post.getAreaId()).isEqualTo(postCreateRequestParam.areaId());
        assertThat(post.getLocationPreference().getAlias()).isEqualTo(postCreateRequestParam.alias());
        assertThat(post.getPostImages().size()).isEqualTo(postCreateRequestParam.files().size());
        assertThat(post.getTitle()).isEqualTo(postCreateRequestParam.title());
        assertThat(post.getContent()).isEqualTo(postCreateRequestParam.content());
        assertThat(post.getPrice()).isEqualTo(postCreateRequestParam.price());
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
    @DisplayName("올바르지 않은 PostId로 조회 시 EntityNotFoundException가 발생하는 것을 확인한다.")
    void findById_inCorrectProductId_EntityNotFoundException() {
        //when
        Exception exception = catchException(() -> postFacade.findById(50L));

        //when
        assertThat(exception).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("게시글을 수정하기 위해서 기존에 작성한 글에 대한 정보를 제대로 불러오는지 확인한다.")
    void findPostInfoToUpdate_createdPost_equals() {
        //given
        PostCreateRequestParam postCreateRequestParam = DataInitializerFactory.getPostCreateRequestParams(setUpMemberId, setUpCategory.getId()).get(1);

        Long productId = postFacade.createPost(postCreateRequestParam);

        //when
        PostToUpdateResponseParam postInfoToUpdate = postFacade.findPostInfoToUpdateById(setUpMemberId, productId);

        //then
        assertThat(postInfoToUpdate.postImages().size()).isEqualTo(postCreateRequestParam.files().size());
        assertThat(postInfoToUpdate.locationPreferenceAlias()).isEqualTo(postCreateRequestParam.alias());
        assertThat(postInfoToUpdate.title()).isEqualTo(postCreateRequestParam.title());
        assertThat(postInfoToUpdate.content()).isEqualTo(postCreateRequestParam.content());
        assertThat(postInfoToUpdate.price()).isEqualTo(postCreateRequestParam.price());
    }

    @Test
    @DisplayName("게시글 수정을 요청한 회원과 게시글 작성자와 다른 경우 예외를 발생시킨다.")
    void findPostInfoToUpdate_notWriter_throwException() {
        //given
        PostCreateRequestParam postCreateRequestParam = DataInitializerFactory.getPostCreateRequestParams(setUpMemberId, setUpCategory.getId()).get(1);
        Long productId = postFacade.createPost(postCreateRequestParam);

        //when_then
        assertThrows(NotWriterException.class, () -> {
            postFacade.findPostInfoToUpdateById(setUpMemberId + 1, productId);
        });
    }

    @Test
    @DisplayName("게시글을 수정 후 제먹과 내용이 입력값과 일치한지 확인한다.")
    void updatePost_response_equals() {
        //given
        List<PostCreateRequestParam> postCreateRequestParams = DataInitializerFactory.getPostCreateRequestParams(setUpMemberId, setUpCategory.getId());
        Long postId = postFacade.createPost(postCreateRequestParams.get(0));
        PostUpdateRequestParam postUpdateRequestParam = DataInitializerFactory.postUpdateRequestParam(postId, setUpCategory.getId());

        //when
        postFacade.updatePost(postUpdateRequestParam);
        Post updatedPost = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("해당 게시글이 없습니다."));

        //then
        assertThat(updatedPost.getId()).isEqualTo(postId);
        assertThat(updatedPost.getTitle()).isEqualTo(postUpdateRequestParam.title());
        assertThat(updatedPost.getContent()).isEqualTo(postUpdateRequestParam.content());
    }

    @Test
    @DisplayName("게시글을 작성한 유저가 게시글 삭제 요청 시 게시글을 성공적으로 삭제한다.")
    void deletePost_MemberIdAndPostID_Success() {
        //given
        PostCreateRequestParam postCreateRequestParam = DataInitializerFactory.getPostCreateRequestParams(setUpMemberId, setUpCategory.getId()).get(1);
        Long postId = postFacade.createPost(postCreateRequestParam);
        Long memberId = postCreateRequestParam.memberId();

        //when
        postFacade.deletePost(memberId, postId);

        //then
        Assertions.assertThatThrownBy(() -> postFacade.findById(postId))
                .isInstanceOf(EntityNotFoundException.class);

    }

    @Test
    @DisplayName("게시글을 삭제 권한이 없는 유저가 게시글 삭제 요청시 예외가 발생한다. ")
    void deletePost_MemberIdAndPostID_Throw() {
        //given
        PostCreateRequestParam postCreateRequestParam = DataInitializerFactory.getPostCreateRequestParams(setUpMemberId, setUpCategory.getId()).get(1);
        Long postId = postFacade.createPost(postCreateRequestParam);
        Long memberId = postCreateRequestParam.memberId();

        //when
        Long strangeMemberId = 5L;
        Assertions.assertThatThrownBy(() -> postFacade.deletePost(strangeMemberId, postId))
                .isInstanceOf(NotWriterException.class);
    }

    /**
     * Member, Category, Product을 미리 setup함.
     */
    private void dataSetup() {
        Member setupMember = DataInitializerFactory.member();
        setUpMemberId = memberService.save(setupMember).id();
        setUpCategory = categoryRepository.save(DataInitializerFactory.category());
        setupProductId = postFacade.createPost(DataInitializerFactory.getPostCreateRequestParams(setUpMemberId, setUpCategory.getId()).get(0));

    }

}
