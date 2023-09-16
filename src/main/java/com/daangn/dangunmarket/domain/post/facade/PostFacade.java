package com.daangn.dangunmarket.domain.post.facade;

import com.daangn.dangunmarket.domain.area.service.AreaService;
import com.daangn.dangunmarket.domain.member.model.ActivityArea;
import com.daangn.dangunmarket.domain.member.service.MemberService;
import com.daangn.dangunmarket.domain.member.service.dto.MemberFindResponse;
import com.daangn.dangunmarket.domain.post.exception.UnauthorizedAccessException;
import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostFindResponseParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostGetResponseParams;
import com.daangn.dangunmarket.domain.post.facade.dto.PostSearchRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostSearchResponseParams;
import com.daangn.dangunmarket.domain.post.facade.dto.PostToUpdateResponseParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostUpdateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.mpper.PostParamDtoMapper;
import com.daangn.dangunmarket.domain.post.facade.mpper.PostParamMapper;
import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.LocationPreference;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.service.CategoryService;
import com.daangn.dangunmarket.domain.post.service.PostImageService;
import com.daangn.dangunmarket.domain.post.service.PostService;
import com.daangn.dangunmarket.domain.post.service.dto.PostFindResponse;
import com.daangn.dangunmarket.domain.post.service.dto.PostGetResponses;
import com.daangn.dangunmarket.domain.post.service.dto.PostSearchConditionRequest;
import com.daangn.dangunmarket.domain.post.service.dto.PostSearchResponses;
import com.daangn.dangunmarket.global.GeometryTypeFactory;
import com.daangn.dangunmarket.global.aws.dto.ImageInfo;
import com.daangn.dangunmarket.global.aws.s3.S3Uploader;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.daangn.dangunmarket.global.response.ErrorCode.POST_NOT_CREATED_BY_USER;

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
    private final PostImageService postImageService;

    public PostFacade(PostService postService, MemberService memberService, AreaService areaService, CategoryService categoryService, S3Uploader s3Uploader, PostParamMapper postParamMapper, PostParamDtoMapper postParamDtoMapper, PostImageService postImageService) {
        this.postService = postService;
        this.memberService = memberService;
        this.areaService = areaService;
        this.categoryService = categoryService;
        this.s3Uploader = s3Uploader;
        this.postParamMapper = postParamMapper;
        this.postParamDtoMapper = postParamDtoMapper;
        this.postImageService = postImageService;
    }

    @Transactional
    public Long createPost(PostCreateRequestParam request) {
        Point point = GeometryTypeFactory.createPoint(request.longitude(), request.latitude());
        LocationPreference locationPreference = new LocationPreference(point, request.alias());

        List<ImageInfo> imageInfos = s3Uploader.saveImages(request.files());
        List<PostImage> postImages = imageInfos.stream()
                .map(p -> new PostImage(p.url(), p.fileName()))
                .toList();

        Category findCategory = categoryService.findById(request.categoryId());

        return postService.createPost(postParamMapper.toPostCreateRequest(
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

    public PostToUpdateResponseParam findPostInfoToUpdateById(Long memberId, Long postId) {

        return postParamDtoMapper.toPostToUpdateResponseParam(postService.getPostInfoToUpdate(memberId, postId));
    }

    public PostGetResponseParams getPosts(Long memberId, Pageable pageable) {
        MemberFindResponse memberResponse = memberService.findById(memberId);
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

        PostGetResponses postResponses = postService.getPosts(areaId, pageable);
        PostGetResponseParams postGetResponseParam = postParamMapper.toPostsGetResponseParam(areaName, postResponses);

        return postGetResponseParam;
    }

    public PostSearchResponseParams getPostsByConditions(PostSearchRequestParam postSearchRequestParam, Long memberId) {
        MemberFindResponse memberFindResponse = memberService.findById(memberId);
        if (!isMemberActivityAreaValid(memberFindResponse.activityAreas())) {
            throw new IllegalStateException("회원의 활동지역이 존재하지 않습니다.");
        }
        Long areaId = memberFindResponse
                .activityAreas()
                .get(0)
                .getId();
        String areaName = areaService
                .findById(areaId)
                .areaName();

        PostSearchConditionRequest searchConditionRequest = postParamMapper.toPostSearchConditionRequest(postSearchRequestParam);

        PostSearchResponses responses = postService.searchPosts(areaId, searchConditionRequest);
        PostSearchResponseParams params = postParamMapper.toPostSearchResponseParams(areaName, responses);

        return params;
    }

    @Transactional
    public void deletePost(Long memberId, Long postId) {
        PostFindResponse response = postService.findById(postId);

        if (!isPostCreatedByUser(response.memberId(), memberId)) {
            throw new UnauthorizedAccessException(POST_NOT_CREATED_BY_USER);
        }

        //1. s3 사진 삭제
        List<String> deleteFileNames = response.postImageList()
                .stream()
                .map(PostImage::getFileName)
                .toList();
        s3Uploader.deleteImages(deleteFileNames);

        //2. 게시물 soft-delete
        postService.deletePost(postId);
    }

    @Transactional
    public Long updatePost(PostUpdateRequestParam request) {

        Point point = GeometryTypeFactory.createPoint(request.longitude(), request.latitude());
        LocationPreference locationPreference = new LocationPreference(point, request.alias());
        Long areaId = areaService.findAreaIdByPolygon(point);

        List<PostImage> postImages = postImageService.saveImagesFromRequest(request.files());
        postImageService.removeImages(request.postId(), request.urls());

        Category findCategory = categoryService.findById(request.categoryId());

        return postService.updatePost(postParamMapper.toPostUpdateRequest(
                request,
                locationPreference,
                postImages,
                findCategory,
                areaId));
    }

    private boolean isMemberActivityAreaValid(List<ActivityArea> activityAreas) {
        return activityAreas.size() > 0;
    }

    private boolean isPostCreatedByUser(Long postCreatorId, Long memberId) {
        return Objects.equals(postCreatorId, memberId);
    }

}
