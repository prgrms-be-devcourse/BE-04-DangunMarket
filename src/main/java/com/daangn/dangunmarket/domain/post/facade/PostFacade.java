package com.daangn.dangunmarket.domain.post.facade;

import com.daangn.dangunmarket.domain.area.service.AreaService;
import com.daangn.dangunmarket.domain.member.model.ActivityArea;
import com.daangn.dangunmarket.domain.member.service.MemberService;
import com.daangn.dangunmarket.domain.member.service.dto.MemberResponse;
import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostFindResponseParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostsGetResponseParam;
import com.daangn.dangunmarket.domain.post.facade.mpper.PostParamMapper;
import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.LocationPreference;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.service.CategoryService;
import com.daangn.dangunmarket.domain.post.service.PostService;
import com.daangn.dangunmarket.domain.post.service.dto.PostFindResponse;
import com.daangn.dangunmarket.domain.post.service.dto.PostGetResponse;
import com.daangn.dangunmarket.global.aws.s3.S3Uploader;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class PostFacade {

    private PostService postService;
    private MemberService memberService;
    private AreaService areaService;
    private CategoryService categoryService;
    private S3Uploader s3Uploader;
    private PostParamMapper mapper;

    public PostFacade(PostService postService,
                      MemberService memberService,
                      AreaService areaService,
                      CategoryService categoryService,
                      S3Uploader s3Uploader,
                      PostParamMapper mapper) {
        this.postService = postService;
        this.memberService = memberService;
        this.areaService = areaService;
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

    public PostFindResponseParam findById(Long productId) {
        PostFindResponse response = postService.findById(productId);

        String memberName = memberService.findById(response.memberId()).nickName();
        String areaName = areaService.findById(response.areaId()).areaName();

        return PostFindResponseParam.of(response, memberName, areaName);
    }

    public PostsGetResponseParam getPosts(Long memberId, Pageable pageable) {
        MemberResponse memberResponse = memberService.findById(memberId);
        if (!isMemberActivityAreaValid(memberResponse.activityAreas())) {
            throw new IllegalStateException("회원의 활동지역이 존재하지 않습니다.");
        }

        Long areaId = memberResponse
                .activityAreas()
                .get(0)
                .getId();
        String areaName = areaService
                .findById(areaId)
                .areaName();

        Page<PostGetResponse> postResponses = postService.getPosts(areaId, pageable);
        PostsGetResponseParam postGetResponseParam = mapper.toPostsGetResponseParam(areaName, postResponses);

        return postGetResponseParam;
    }

    private boolean isMemberActivityAreaValid(List<ActivityArea> activityAreas) {
        return activityAreas.size() > 0;
    }

}
