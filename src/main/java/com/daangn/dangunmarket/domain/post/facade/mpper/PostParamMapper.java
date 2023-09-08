package com.daangn.dangunmarket.domain.post.facade.mpper;

import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.LocationPreference;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.service.dto.PostCreateRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostParamMapper {

    public PostCreateRequest toPostCreateRequest(
            PostCreateRequestParam requestParam,
            LocationPreference locationPreference,
            List<PostImage> postImages,
            Category category) {
        return new PostCreateRequest(
                requestParam.memberId(),
                requestParam.areaId(),
                locationPreference,
                postImages,
                category,
                requestParam.title(),
                requestParam.content(),
                requestParam.price(),
                requestParam.isOfferAllowed());
    }

}
