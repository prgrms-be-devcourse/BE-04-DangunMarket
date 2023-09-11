package com.daangn.dangunmarket.domain.post.controller.mapper;

import com.daangn.dangunmarket.domain.post.controller.dto.PostCreateApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.PostLikeApiResponse;
import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.service.dto.PostLikeResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-12T02:59:34+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class PostApiMapperImpl implements PostApiMapper {

    @Override
    public PostCreateRequestParam toPostCreateRequestParam(PostCreateApiRequest request, Long memberId) {
        if ( request == null && memberId == null ) {
            return null;
        }

        Long areaId = null;
        Double latitude = null;
        Double longitude = null;
        String alias = null;
        List<MultipartFile> files = null;
        Long categoryId = null;
        String title = null;
        String content = null;
        Long price = null;
        boolean isOfferAllowed = false;
        LocalDateTime refreshedAt = null;
        if ( request != null ) {
            areaId = request.areaId();
            latitude = request.latitude();
            longitude = request.longitude();
            alias = request.alias();
            List<MultipartFile> list = request.files();
            if ( list != null ) {
                files = new ArrayList<MultipartFile>( list );
            }
            categoryId = request.categoryId();
            title = request.title();
            content = request.content();
            price = request.price();
            isOfferAllowed = request.isOfferAllowed();
            refreshedAt = request.refreshedAt();
        }
        Long memberId1 = null;
        memberId1 = memberId;

        PostCreateRequestParam postCreateRequestParam = new PostCreateRequestParam( memberId1, areaId, latitude, longitude, alias, files, categoryId, title, content, price, isOfferAllowed, refreshedAt );

        return postCreateRequestParam;
    }

    @Override
    public PostLikeApiResponse toPostLikeApiResponse(PostLikeResponse response) {
        if ( response == null ) {
            return null;
        }

        int likeCount = 0;
        boolean isLiked = false;

        likeCount = response.likeCount();
        isLiked = response.isLiked();

        PostLikeApiResponse postLikeApiResponse = new PostLikeApiResponse( likeCount, isLiked );

        return postLikeApiResponse;
    }
}
