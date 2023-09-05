package com.daangn.dangunmarket.domain.product.product.controller.mapper;

import com.daangn.dangunmarket.domain.product.product.controller.dto.ProductCreateApiRequest;
import com.daangn.dangunmarket.domain.product.product.facade.dto.ProductCreateRequestParam;
import org.springframework.stereotype.Component;

@Component
public class ProductApiMapper {

    public ProductCreateRequestParam toProductCreateRequest(ProductCreateApiRequest request) {
        return new ProductCreateRequestParam(
                request.memberId(),
                request.latitude(),
                request.longitude(),
                request.alias(),
                request.files(),
                request.categoryId(),
                request.title(),
                request.content(),
                request.price(),
                request.isOfferAllowed(),
                request.refreshedAt());
    }

}
