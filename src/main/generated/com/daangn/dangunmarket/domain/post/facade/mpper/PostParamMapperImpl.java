package com.daangn.dangunmarket.domain.post.facade.mpper;

import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.LocationPreference;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.service.dto.PostCreateRequest;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-12T02:59:33+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class PostParamMapperImpl implements PostParamMapper {

    @Override
    public PostCreateRequest toPostCreateRequest(PostCreateRequestParam requestParam, LocationPreference locationPreference, List<PostImage> postImages, Category category) {
        if ( requestParam == null && locationPreference == null && postImages == null && category == null ) {
            return null;
        }

        Long memberId = null;
        Long areaId = null;
        String title = null;
        String content = null;
        Long price = null;
        boolean isOfferAllowed = false;
        if ( requestParam != null ) {
            memberId = requestParam.memberId();
            areaId = requestParam.areaId();
            title = requestParam.title();
            content = requestParam.content();
            price = requestParam.price();
            isOfferAllowed = requestParam.isOfferAllowed();
        }
        LocationPreference locationPreference1 = null;
        locationPreference1 = locationPreference;
        List<PostImage> postImages1 = null;
        List<PostImage> list = postImages;
        if ( list != null ) {
            postImages1 = new ArrayList<PostImage>( list );
        }
        Category category1 = null;
        category1 = category;

        PostCreateRequest postCreateRequest = new PostCreateRequest( memberId, areaId, locationPreference1, postImages1, category1, title, content, price, isOfferAllowed );

        return postCreateRequest;
    }
}
