package com.daangn.dangunmarket.domain.post.facade;

import com.daangn.dangunmarket.domain.area.service.AreaService;
import com.daangn.dangunmarket.domain.member.service.MemberService;
import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostFindResponseParam;
import com.daangn.dangunmarket.domain.post.facade.mpper.PostParamMapper;
import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.LocationPreference;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.service.CategoryService;
import com.daangn.dangunmarket.domain.post.service.PostService;
import com.daangn.dangunmarket.domain.post.service.dto.PostFindResponse;
import com.daangn.dangunmarket.global.GeometryTypeFactory;
import com.daangn.dangunmarket.global.aws.s3.S3Uploader;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class PostFacade {

    private PostService postService;
    private MemberService memberService;
    private AreaService areaService;
    private CategoryService categoryService;
    private S3Uploader s3Uploader;
    private PostParamMapper mapper;


    public PostFacade(PostService postService, MemberService memberService, AreaService areaService, CategoryService categoryService, S3Uploader s3Uploader, PostParamMapper mapper) {
        this.postService = postService;
        this.memberService = memberService;
        this.areaService = areaService;
        this.categoryService = categoryService;
        this.s3Uploader = s3Uploader;
        this.mapper = mapper;
    }

    @Transactional
    public Long createPost(PostCreateRequestParam request) {
        Point point = GeometryTypeFactory.createPoint(request.longitude(), request.latitude());
        LocationPreference locationPreference = new LocationPreference(point, request.alias());

        List<String> url = s3Uploader.saveImages(request.files());
        List<PostImage> postImages = url.stream()
                .map(PostImage::new)
                .toList();

        Category findCategory = categoryService.findById(request.categoryId());

        return postService.createPost(mapper.toPostCreateRequest(
                request,
                locationPreference,
                postImages,
                findCategory));
    }


    public PostFindResponseParam findById(Long productId) {
        PostFindResponse response = postService.findById(productId);

        String memberName = memberService.findById(response.memberId()).nickName();
        String areaName = areaService.findById(response.areaId()).areaName();

        return PostFindResponseParam.of(response, memberName, areaName);
    }

}
