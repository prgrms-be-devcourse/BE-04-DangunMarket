package com.daangn.dangunmarket.domain.product.facade;

import com.daangn.dangunmarket.domain.category.domain.Category;
import com.daangn.dangunmarket.domain.category.domain.CategoryRepository;
import com.daangn.dangunmarket.domain.category.service.CategoryService;
import com.daangn.dangunmarket.domain.product.model.Product;
import com.daangn.dangunmarket.domain.product.repository.JpaProductRepository;
import com.daangn.dangunmarket.domain.product.facade.dto.ProductCreateRequestParam;
import com.daangn.dangunmarket.domain.product.facade.mpper.ProductParamMapper;
import com.daangn.dangunmarket.domain.product.service.ProductService;
import com.daangn.dangunmarket.global.aws.s3.S3UploaderInterface;
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
    private CategoryService categoryService;
    @MockBean
    private S3UploaderInterface s3UploaderInterface;
    @Autowired
    private ProductParamMapper mapper;

    @Autowired
    private JpaProductRepository jpaProductRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private Category setupCategory;

    @BeforeEach
    void setup(){
        productFacade = new ProductFacade(productService,
                categoryService,
                s3UploaderInterface,
                mapper);

        setupCategory = categoryRepository.save(new Category("전자기기", null, 1L, null));
    }

    @Test
    @DisplayName("product를 저장 후 저장된 product불러와 필드값들을 확인한다.")
    void createProduct_correctRequest_ProductCreateResponse() {
        //given
        given(s3UploaderInterface.saveImages(any())).willReturn(List.of("a", "b", "c"));
        List<MultipartFile> mockMultipartFiles = List.of(new MockMultipartFile("first", (byte[]) null), new MockMultipartFile("second", (byte[]) null));
        ProductCreateRequestParam requestParam = new ProductCreateRequestParam(1L, 2L,37.5297,126.8876,  "seoul", mockMultipartFiles, setupCategory.getId(), "firstTile", "firstContent", 100L, true, LocalDateTime.now());

        //when
        Long productId = productFacade.createProduct(requestParam);

        //when
        Product product = jpaProductRepository.findById(productId).orElseThrow();
        assertThat(product.getMemberId()).isEqualTo(1L);
        assertThat(product.getAreaId()).isEqualTo(2l);
        assertThat(product.getLocalPreference().getAlias()).isEqualTo("seoul");
        assertThat(product.getProductImageList().size()).isEqualTo(3);
        assertThat(product.getTitle()).isEqualTo("firstTile");
        assertThat(product.getContent()).isEqualTo("firstContent");
        assertThat(product.getPrice()).isEqualTo(100L);
    }
}