package com.daangn.dangunmarket.domain.product.repository;

import com.daangn.dangunmarket.domain.product.model.Product;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository{

    private JpaProductRepository jpaProductRepository;

    public ProductRepositoryImpl(JpaProductRepository jpaProductRepository) {
        this.jpaProductRepository = jpaProductRepository;
    }

    @Override
    public Product save(Product product) {
        return jpaProductRepository.save(product);
    }

}
