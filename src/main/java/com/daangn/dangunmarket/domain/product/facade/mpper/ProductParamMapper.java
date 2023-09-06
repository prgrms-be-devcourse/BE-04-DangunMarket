package com.daangn.dangunmarket.domain.product.facade.mpper;

import com.daangn.dangunmarket.domain.product.facade.dto.ProductCreateRequestParam;
import com.daangn.dangunmarket.domain.product.model.Category;
import com.daangn.dangunmarket.domain.product.model.LocationPreference;
import com.daangn.dangunmarket.domain.product.service.dto.ProductCreateRequest;
import com.daangn.dangunmarket.domain.product.model.ProductImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductParamMapper {

    public ProductCreateRequest toProductCreateRequest(
            ProductCreateRequestParam requestParam,
            LocationPreference locationPreference,
            List<ProductImage> productImages,
            Category category) {
        return new ProductCreateRequest(
                requestParam.memberId(),
                requestParam.areaId(),
                locationPreference,
                productImages,
                category,
                requestParam.title(),
                requestParam.content(),
                requestParam.price(),
                requestParam.isOfferAllowed());
    }

}
