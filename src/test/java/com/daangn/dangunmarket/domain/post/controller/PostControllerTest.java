package com.daangn.dangunmarket.domain.post.controller;

import com.daangn.dangunmarket.domain.AbstractRestDocsTests;
import com.daangn.dangunmarket.domain.post.controller.mapper.PostApiMapper;
import com.daangn.dangunmarket.domain.post.facade.PostFacade;
import com.daangn.dangunmarket.domain.post.facade.dto.PostFindResponseParam;
import com.daangn.dangunmarket.domain.post.model.TradeStatus;
import com.daangn.dangunmarket.domain.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(PostController.class)
class PostControllerTest extends AbstractRestDocsTests {

    @MockBean
    private PostFacade postFacade;

    @MockBean
    private PostService postService;

    @MockBean
    private PostApiMapper postApiMapper;

    @Test
    void createProduct() {
    }

    @Test
    @DisplayName("게시글 1개를 조회 한다.")
    void findById() throws Exception {
        //given
        BDDMockito.given(postFacade.findById(any()))
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

        mockMvc.perform(get("/posts/1"))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("post/findById",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(MockMvcResultMatchers.status().isOk());
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

    @Override
    protected Object initController() {
        return new PostController(postFacade, postService, postApiMapper);
    }
}