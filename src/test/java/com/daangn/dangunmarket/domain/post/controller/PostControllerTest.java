package com.daangn.dangunmarket.domain.post.controller;

import com.daangn.dangunmarket.domain.auth.jwt.CustomUser;
import com.daangn.dangunmarket.domain.post.controller.mapper.PostApiMapper;
import com.daangn.dangunmarket.domain.post.facade.PostFacade;
import com.daangn.dangunmarket.domain.post.facade.dto.PostFindResponseParam;
import com.daangn.dangunmarket.domain.post.model.TradeStatus;
import com.daangn.dangunmarket.domain.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostFacade postFacade;

    @MockBean
    private PostService postService;

    @MockBean
    private PostApiMapper postApiMapper;

    @Test
    void createProduct() throws Exception {
        //given
        given(postFacade.createPost(any())).willReturn(1L);
        setMockCustomUser();

        MockMultipartFile requestFile = new MockMultipartFile(
                "files",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes());

        MockMultipartFile requestJson = new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE,
                "{\"areaId\": 1, \"latitude\": 37.5665, \"longitude\": 126.978, \"alias\": \"alias\", \"categoryId\": 1, \"title\": \"Test Title\", \"content\": \"Test Description\", \"price\": 10000, \"isOfferAllowed\": true}".getBytes());

        //when //then
        mockMvc.perform(multipart("/posts")
                        .file(requestFile)
                        .file(requestJson)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.postId").value(1L))
                .andDo(document("post/createProduct",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("files").description("게시글에 첨부할 이미지 파일"),
                                partWithName("request").description("게시글 생성 요청 정보")
                        ),
                        requestPartFields(
                                "request",
                                fieldWithPath("areaId").type(JsonFieldType.NUMBER).description("지역 id"),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("경도"),
                                fieldWithPath("alias").type(JsonFieldType.STRING).description("거래 희망 장소의 이름"),
                                fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("category의 id"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시글의 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시글의 내용"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                                fieldWithPath("isOfferAllowed").type(JsonFieldType.BOOLEAN).description("가격 제안 가능 여부")
                        ),
                        responseFields(
                                fieldWithPath("postId").type(JsonFieldType.NUMBER).description("생성된 post의 id")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 1개를 조회 한다.")
    void findById() throws Exception {
        //given
        given(postFacade.findById(any(Long.class)))
                .willReturn(new PostFindResponseParam(
                        1L,
                        "윤영운",
                        "역삼동",
                        35.78,
                        78.79,
                        "강남역 앞",
                        List.of("https://aws-asd", "https://aws-abc"),
                        3L,
                        "전자제품",
                        TradeStatus.IN_PROGRESS,
                        "에어팟 프로 팝니다.",
                        "에어팟 프로2가 2개 생겨 1개 팝니다.",
                        250000,
                        true,
                        LocalDateTime.of(2023, 9, 20, 0, 0),
                        5));

        Long postId = 1L;

        //when //then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/posts/{postId}", postId))
                .andExpect(status().isOk())
                .andDo(document("post/findById",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("게시글 id")
                        ),
                        responseFields(
                                fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글 id"),
                                fieldWithPath("memberName").type(JsonFieldType.STRING).description("게시글 주인의 닉네임"),
                                fieldWithPath("areaName").type(JsonFieldType.STRING).description("게시글 주인의 활동 지역 이름"),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("거래 장소의 위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("거래 장소의 경도"),
                                fieldWithPath("locationPreferenceAlias").type(JsonFieldType.STRING).description("거래 장소 이름"),
                                fieldWithPath("urls[]").type(JsonFieldType.ARRAY).description("거래 장소 이름"),
                                fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("카테고리 id"),
                                fieldWithPath("categoryName").type(JsonFieldType.STRING).description("카테고리 이름"),
                                fieldWithPath("tradeStatus").type(JsonFieldType.STRING).description("거래 상태"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                                fieldWithPath("isOfferAllowed").type(JsonFieldType.BOOLEAN).description("가격 제안 가능 여부"),
                                fieldWithPath("refreshedAt").type(JsonFieldType.STRING).description("끌올 시간"),
                                fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("좋아요 개수")
                        )
                ));
    }

    @Test
    void changeStatus() {
    }

    @Test
    void refreshPostTime() {
    }

    @Test
    void findPostInfoToUpdate() {
    }

    @Test
    void getPosts() {
    }

    @Test
    void updatePost() {
    }

    @Test
    void getPostsByConditions() {
    }

    @Test
    void deletePost() {
    }

    @Test
    void addLikePost() {
    }

    @Test
    void cancelLikePost() {
    }

    private void setMockCustomUser() {
        CustomUser customUser = new CustomUser(1L, "asdfasfsag", null);
        Authentication authentication = new TestingAuthenticationToken(customUser, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
