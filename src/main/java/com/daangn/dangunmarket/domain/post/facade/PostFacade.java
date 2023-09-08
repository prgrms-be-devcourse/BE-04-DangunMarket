package com.daangn.dangunmarket.domain.post.facade;

import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.mpper.PostParamMapper;
import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.LocationPreference;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.service.CategoryService;
import com.daangn.dangunmarket.domain.post.service.PostService;
import com.daangn.dangunmarket.global.aws.s3.S3Uploader;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class PostFacade {

    private PostService postService;
    private CategoryService categoryService;
    private S3Uploader s3Uploader;
    private PostParamMapper mapper;

    public PostFacade(PostService postService, CategoryService categoryService, S3Uploader s3Uploader, PostParamMapper mapper) {
        this.postService = postService;
        this.categoryService = categoryService;
        this.s3Uploader = s3Uploader;
        this.mapper = mapper;
    }

    @Transactional
    public Long createPost(PostCreateRequestParam reqest) {
        GeometryFactory factory = new GeometryFactory();
        Point point = factory.createPoint(new Coordinate(reqest.longitude(), reqest.latitude()));
        LocationPreference locationPreference = new LocationPreference(point, reqest.alias());

        List<String> url = s3Uploader.saveImages(reqest.files());
        List<PostImage> postImages = url.stream()
                .map(PostImage::new)
                .collect(Collectors.toList());

        Category findCategory = categoryService.findById(reqest.categoryId());

        return postService.createPost(mapper.toPostCreateRequest(
                reqest,
                locationPreference,
                postImages,
                findCategory));
    }

}
