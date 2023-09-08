package com.daangn.dangunmarket.domain.post.service.mapper;

import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.model.TradeStatus;
import com.daangn.dangunmarket.domain.post.model.vo.Price;
import com.daangn.dangunmarket.domain.post.model.vo.Title;
import com.daangn.dangunmarket.domain.post.service.dto.PostCreateRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PostMapper {

    public Post toEntity(PostCreateRequest reqest) {
        return Post.builder()
                .memberId(reqest.memberId())
                .areaId(reqest.areaId())
                .localPreference(reqest.locationPreference())
                .postImageList(reqest.postImages())
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
