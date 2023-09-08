package com.daangn.dangunmarket.domain.product.service.mapper;

import com.daangn.dangunmarket.domain.product.model.Product;
import com.daangn.dangunmarket.domain.product.model.TradeStatus;
import com.daangn.dangunmarket.domain.product.model.vo.Price;
import com.daangn.dangunmarket.domain.product.model.vo.Title;
import com.daangn.dangunmarket.domain.product.service.dto.ProductCreateRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductMapper {

    public Product toEntity(ProductCreateRequest reqest) {
        return Product.builder()
                .memberId(reqest.memberId())
                .areaId(reqest.areaId())
                .localPreference(reqest.locationPreference())
                .productImageList(reqest.productImages())
                .category(reqest.category())
                .tradeStatus(TradeStatus.IN_PROGRESS)
                .title(new Title(reqest.title()))
                .content(reqest.content())
                .price(new Price(reqest.price()))
                .isOfferAllowed(reqest.isOfferAllowed())
                .refreshedAt(LocalDateTime.now())
                .build();
    }

}