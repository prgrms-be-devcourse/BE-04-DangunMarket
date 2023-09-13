package com.daangn.dangunmarket.domain.post.facade;

import com.daangn.dangunmarket.domain.area.service.AreaService;
import com.daangn.dangunmarket.domain.member.service.MemberService;
import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostFindResponseParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostToUpdateResponseParam;
import com.daangn.dangunmarket.domain.post.facade.mpper.PostParamDtoMapper;
import com.daangn.dangunmarket.domain.post.facade.mpper.PostParamMapper;
import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.LocationPreference;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.service.CategoryService;
import com.daangn.dangunmarket.domain.post.service.PostService;
import com.daangn.dangunmarket.domain.post.service.dto.PostFindResponse;
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

    private final PostService postService;
    private final MemberService memberService;
    private final AreaService areaService;
    private final CategoryService categoryService;
    private final S3Uploader s3Uploader;
    private final PostParamMapper postParamMapper;
    private final PostParamDtoMapper postParamDtoMapper;


    public PostFacade(PostService postService, MemberService memberService, AreaService areaService, CategoryService categoryService, S3Uploader s3Uploader, PostParamMapper postParamMapper, PostParamDtoMapper postParamDtoMapper) {
        this.postService = postService;
        this.memberService = memberService;
        this.areaService = areaService;
        this.categoryService = categoryService;
        this.s3Uploader = s3Uploader;
        this.postParamMapper = postParamMapper;
        this.postParamDtoMapper = postParamDtoMapper;
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

        return postService.createPost(postParamMapper.toPostCreateRequest(
                reqest,
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

    public PostToUpdateResponseParam findPostInfoToUpdateById(Long memberId, Long postId) {

        return postParamDtoMapper.toPostToUpdateResponseParam(postService.getPostInfoToUpdate(memberId,postId));

    }

}
