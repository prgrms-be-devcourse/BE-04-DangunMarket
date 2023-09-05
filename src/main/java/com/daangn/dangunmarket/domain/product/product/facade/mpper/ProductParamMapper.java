package com.daangn.dangunmarket.domain.product.product.facade.mpper;

import com.daangn.dangunmarket.domain.category.domain.Category;
import com.daangn.dangunmarket.domain.member.member.domain.Member;
import com.daangn.dangunmarket.domain.product.locationPreference.domain.LocationPreference;
import com.daangn.dangunmarket.domain.product.product.facade.dto.ProductCreateRequestParam;
import com.daangn.dangunmarket.domain.product.product.service.dto.ProductCreateRequest;
import com.daangn.dangunmarket.domain.product.productImage.domain.ProductImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductParamMapper {

    public ProductCreateRequest toProductCreateRequest(
            ProductCreateRequestParam requestParam,
            Member member,
            LocationPreference locationPreference,
            List<ProductImage> productImages,
            Category category) {
        return new ProductCreateRequest(
                member,
                locationPreference,
                productImages,
                category,
                requestParam.title(),
                requestParam.content(),
                requestParam.price(),
                requestParam.isOfferAllowed());
    }

}
