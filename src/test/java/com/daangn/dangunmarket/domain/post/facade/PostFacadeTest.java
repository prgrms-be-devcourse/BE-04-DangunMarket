package com.daangn.dangunmarket.domain.post.facade;

import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.mpper.PostParamMapper;
import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.repository.post.CategoryRepository;
import com.daangn.dangunmarket.domain.post.repository.post.PostJpaRepository;
import com.daangn.dangunmarket.domain.post.service.CategoryService;
import com.daangn.dangunmarket.domain.post.service.PostService;
import com.daangn.dangunmarket.global.aws.s3.S3Uploader;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
    private CategoryService categoryService;

    @MockBean
    private S3Uploader s3Uploader;

    @Autowired
    private PostParamMapper mapper;

    @Autowired
    private PostJpaRepository postJpaRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category setupCategory;

    @BeforeEach
    void setup() {
        postFacade = new PostFacade(postService,
                categoryService,
                s3Uploader,
                mapper);

        setupCategory = categoryRepository.save(new Category("전자기기", null, 1L, null));
    }

    @Test
    @DisplayName("product를 저장 후 저장된 product불러와 필드값들을 확인한다.")
    void createProduct_correctRequest_ProductCreateResponse() {
        //given
        given(s3Uploader.saveImages(any())).willReturn(List.of("a", "b", "c"));
        List<MultipartFile> mockMultipartFiles = List.of(new MockMultipartFile("first", (byte[]) null), new MockMultipartFile("second", (byte[]) null));
        PostCreateRequestParam requestParam = new PostCreateRequestParam(1L, 2L, 37.5297, 126.8876, "seoul", mockMultipartFiles, setupCategory.getId(), "firstTile", "firstContent", 100L, true, LocalDateTime.now());

        //when
        Long productId = postFacade.createPost(requestParam);

        //then
        Post post = postJpaRepository.findById(productId).orElseThrow();
        assertThat(post.getMemberId()).isEqualTo(1L);
        assertThat(post.getAreaId()).isEqualTo(2l);
        assertThat(post.getLocalPreference().getAlias()).isEqualTo("seoul");
        assertThat(post.getPostImageList().size()).isEqualTo(3);
        assertThat(post.getTitle()).isEqualTo("firstTile");
        assertThat(post.getContent()).isEqualTo("firstContent");
        assertThat(post.getPrice()).isEqualTo(100L);
    }
}
