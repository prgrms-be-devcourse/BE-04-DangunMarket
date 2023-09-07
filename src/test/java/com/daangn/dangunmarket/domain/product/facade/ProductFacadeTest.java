package com.daangn.dangunmarket.domain.product.facade;

import com.daangn.dangunmarket.domain.area.service.AreaService;
import com.daangn.dangunmarket.domain.area.service.dto.AreaResponse;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.model.MemberProvider;
import com.daangn.dangunmarket.domain.member.model.NickName;
import com.daangn.dangunmarket.domain.member.model.RoleType;
import com.daangn.dangunmarket.domain.member.repository.MemberJpaRepository;
import com.daangn.dangunmarket.domain.member.service.MemberService;
import com.daangn.dangunmarket.domain.product.facade.dto.ProductFindResponseParam;
import com.daangn.dangunmarket.domain.product.model.Category;
import com.daangn.dangunmarket.domain.product.repository.CategoryRepository;
import com.daangn.dangunmarket.domain.product.repository.ProductRepository;
import com.daangn.dangunmarket.domain.product.service.CategoryService;
import com.daangn.dangunmarket.domain.product.model.Product;
import com.daangn.dangunmarket.domain.product.facade.dto.ProductCreateRequestParam;
import com.daangn.dangunmarket.domain.product.facade.mpper.ProductParamMapper;
import com.daangn.dangunmarket.domain.product.service.ProductService;
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
class ProductFacadeTest {

    private ProductFacade productFacade;

    @Autowired
    private ProductService productService;

    @Autowired
    private MemberService memberService;

    @MockBean
    private AreaService areaService;

    @Autowired
    private CategoryService categoryService;

    @MockBean
    private S3Uploader s3Uploader;

    @Autowired
    private ProductParamMapper mapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Long setupProductId;

    private Category setupCategory;

    @BeforeEach
    void setup(){
        productFacade = new ProductFacade(
                productService,
                memberService,
                areaService,
                categoryService,
                s3Uploader,
                mapper);

        given(s3Uploader.saveImages(any())).willReturn(List.of("z", "s"));

        dataSetup();
    }

    @Test
    @DisplayName("product를 저장 후 저장된 product불러와 필드값들을 확인한다.")
    void createProduct_correctRequest_Long() {
        //given
        given(s3Uploader.saveImages(any())).willReturn(List.of("a", "b", "c"));
        List<MultipartFile> mockMultipartFiles = List.of(new MockMultipartFile("first", (byte[]) null), new MockMultipartFile("second", (byte[]) null));
        ProductCreateRequestParam requestParam = new ProductCreateRequestParam(1L, 2L,37.5297,126.8876,  "seoul", mockMultipartFiles, setupCategory.getId(), "firstTile", "firstContent", 100L, true, LocalDateTime.now());

        //when
        Long productId = productFacade.createProduct(requestParam);

        //then
        Product product = productRepository.findById(productId).orElseThrow();
        assertThat(product.getMemberId()).isEqualTo(1L);
        assertThat(product.getAreaId()).isEqualTo(2l);
        assertThat(product.getLocalPreference().getAlias()).isEqualTo("seoul");
        assertThat(product.getProductImageList().size()).isEqualTo(3);
        assertThat(product.getTitle()).isEqualTo("firstTile");
        assertThat(product.getContent()).isEqualTo("firstContent");
        assertThat(product.getPrice()).isEqualTo(100L);
    }

    @Test
    @DisplayName("setup된 Product의 ID를 통해 조회하여 응답값을 검사한다.")
    void findById_correctProductId_ProductFindResponseParam(){
        //given
        AreaResponse setupAreaResponse = new AreaResponse(1L, "12345", "서울시 강남구 청담동", LocalDateTime.now());
        given(areaService.findById(any())).willReturn(setupAreaResponse);

        //when
        ProductFindResponseParam responseParam = productFacade.findById(setupProductId);

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
    }

    /**
     * Member, Category, Product을 미리 setup함.
     */
    private void dataSetup() {
        Member setupMember = new Member(2L, RoleType.USER, MemberProvider.GOOGLE, "abcde", new NickName("james"), 37);
        memberJpaRepository.save(setupMember);

        Category setupCategory = new Category("전자기기", null, 1L, null);
        this.setupCategory = categoryRepository.save(setupCategory);

        List<MultipartFile> mockMultipartFiles = List.of(new MockMultipartFile("third", (byte[]) null), new MockMultipartFile("four", (byte[]) null));
        ProductCreateRequestParam requestParam = new ProductCreateRequestParam(2L, 1L,53.5297,126.8876,  "네이버 그린 팩토리", mockMultipartFiles, this.setupCategory.getId(), "SetupTile", "SetupContent", 200L, true, LocalDateTime.now());
        setupProductId = productFacade.createProduct(requestParam);
    }

}
