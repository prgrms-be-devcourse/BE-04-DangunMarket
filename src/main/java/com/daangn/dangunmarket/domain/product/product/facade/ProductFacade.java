package com.daangn.dangunmarket.domain.product.product.facade;

import com.daangn.dangunmarket.domain.category.domain.Category;
import com.daangn.dangunmarket.domain.category.service.CategoryService;
import com.daangn.dangunmarket.domain.member.member.domain.Member;
import com.daangn.dangunmarket.domain.product.locationPreference.domain.LocationPreference;
import com.daangn.dangunmarket.domain.product.product.facade.mpper.ProductParamMapper;
import com.daangn.dangunmarket.domain.product.product.service.ProductService;
import com.daangn.dangunmarket.domain.product.product.facade.dto.ProductCreateRequestParam;
import com.daangn.dangunmarket.domain.product.productImage.domain.ProductImage;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class ProductFacade {

    private ProductService productService;
    private MemberService memberService;
    private CategoryService categoryService;
    private S3Service s3Service;
    private ProductParamMapper mapper;

    public ProductFacade(ProductService productService, MemberService memberService, CategoryService categoryService, S3Service s3Service, ProductParamMapper mapper) {
        this.productService = productService;
        this.memberService = memberService;
        this.categoryService = categoryService;
        this.s3Service = s3Service;
        this.mapper = mapper;
    }

    @Transactional
    public Long createProduct(ProductCreateRequestParam reqest){
        Member findMember = memberService.findById(reqest.memberId());
        LocationPreference locationPreference = new LocationPreference(new Point(reqest.longitude(), reqest.latitude()), reqest.alias());

        List<String> url = s3Service.saves(reqest.files());
        List<ProductImage> productImages = url.stream()
                                                .map(ProductImage::new)
                                                .collect(Collectors.toList());

        Category findCategory = categoryService.findById(reqest.categoryId());

        return productService.createProduct(mapper.toProductCreateRequest(
                reqest,
                findMember,
                locationPreference,
                productImages,
                findCategory));
    }

}
