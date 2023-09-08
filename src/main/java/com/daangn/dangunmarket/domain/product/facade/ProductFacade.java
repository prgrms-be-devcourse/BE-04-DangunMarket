package com.daangn.dangunmarket.domain.product.facade;

import com.daangn.dangunmarket.domain.product.service.CategoryService;
import com.daangn.dangunmarket.domain.product.model.Category;
import com.daangn.dangunmarket.domain.product.model.LocationPreference;
import com.daangn.dangunmarket.domain.product.facade.mpper.ProductParamMapper;
import com.daangn.dangunmarket.domain.product.service.ProductService;
import com.daangn.dangunmarket.domain.product.facade.dto.ProductCreateRequestParam;
import com.daangn.dangunmarket.domain.product.model.ProductImage;
import com.daangn.dangunmarket.global.aws.s3.S3Uploader;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class ProductFacade {

    private ProductService productService;
    private CategoryService categoryService;
    private S3Uploader s3Uploader;
    private ProductParamMapper mapper;

    public ProductFacade(ProductService productService, CategoryService categoryService, S3Uploader s3Uploader, ProductParamMapper mapper) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.s3Uploader = s3Uploader;
        this.mapper = mapper;
    }

    @Transactional
    public Long createProduct(ProductCreateRequestParam reqest) {
        GeometryFactory factory = new GeometryFactory();
        Point point = factory.createPoint(new Coordinate(reqest.longitude(), reqest.latitude()));
        LocationPreference locationPreference = new LocationPreference(point, reqest.alias());

        List<String> url = s3Uploader.saveImages(reqest.files());
        List<ProductImage> productImages = url.stream()
                .map(ProductImage::new)
                .collect(Collectors.toList());

        Category findCategory = categoryService.findById(reqest.categoryId());

        return productService.createProduct(mapper.toProductCreateRequest(
                reqest,
                locationPreference,
                productImages,
                findCategory));
    }

}