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

    public Post toEntity(PostCreateRequest request) {
        return Post.builder()
                .memberId(request.memberId())
                .areaId(request.areaId())
                .localPreference(request.locationPreference())
                .postImages(request.postImages())
                .category(request.category())
                .tradeStatus(TradeStatus.IN_PROGRESS)
                .title(new Title(request.title()))
                .content(request.content())
                .price(new Price(request.price()))
                .isOfferAllowed(request.isOfferAllowed())
                .refreshedAt(LocalDateTime.now())
                .likeCount(0)
                .build();
    }

}
