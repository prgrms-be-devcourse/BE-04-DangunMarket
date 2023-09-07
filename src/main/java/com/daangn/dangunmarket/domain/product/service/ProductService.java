package com.daangn.dangunmarket.domain.product.service;

import com.daangn.dangunmarket.domain.product.model.Product;
import com.daangn.dangunmarket.domain.product.repository.ProductRepository;
import com.daangn.dangunmarket.domain.product.service.dto.ProductCreateRequest;
import com.daangn.dangunmarket.domain.product.service.dto.ProductFindResponse;
import com.daangn.dangunmarket.domain.product.service.mapper.ProductMapper;
import com.daangn.dangunmarket.global.Exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.daangn.dangunmarket.global.response.ErrorCode.NOT_FOUND_PRODUCT_ENTITY;

@Transactional(readOnly = true)
@Service
public class ProductService {

    private ProductRepository productRepository;
    private ProductMapper mapper;

    public ProductService(ProductRepository productRepository, ProductMapper mapper) {
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @Transactional
    public Long createProduct(ProductCreateRequest request) {
        Product saveProduct = productRepository.save(mapper.toEntity(request));
        return saveProduct.getId();
    }

    public ProductFindResponse findById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_PRODUCT_ENTITY));

        return ProductFindResponse.from(product);
    }
}
