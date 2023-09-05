package com.daangn.dangunmarket.domain.product.product.service;

import com.daangn.dangunmarket.domain.product.product.domain.Product;
import com.daangn.dangunmarket.domain.product.product.domain.ProductRepository;
import com.daangn.dangunmarket.domain.product.product.service.dto.ProductCreateRequest;
import com.daangn.dangunmarket.domain.product.product.service.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Long createProduct(ProductCreateRequest reqest){
        Product saveProduct = productRepository.save(mapper.toEntity(reqest));
        return saveProduct.getId();
    }

}
