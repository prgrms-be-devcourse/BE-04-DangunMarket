package com.daangn.dangunmarket.domain.post.controller.mapper;

import com.daangn.dangunmarket.domain.post.controller.dto.PostCreateApiRequest;
import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-09T18:55:02+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class PostApiMapperImpl implements PostApiMapper {

    @Override
    public PostCreateRequestParam toPostCreateRequestParam(PostCreateApiRequest request) {
        if ( request == null ) {
            return null;
        }

        Long memberId = null;
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

        memberId = request.memberId();
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

        PostCreateRequestParam postCreateRequestParam = new PostCreateRequestParam( memberId, areaId, latitude, longitude, alias, files, categoryId, title, content, price, isOfferAllowed, refreshedAt );

        return postCreateRequestParam;
    }
}
