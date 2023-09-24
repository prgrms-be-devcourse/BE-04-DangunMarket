package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.domain.DataInitializerFactory;
import com.daangn.dangunmarket.domain.chat.model.ChatRoom;
import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.chat.repository.chatroom.ChatRoomRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.ChatRoomInfoRepository;
import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.repository.category.CategoryRepository;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ChatRoomInfoServiceTest {

    @Autowired
    ChatRoomInfoService chatRoomInfoService;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    ChatRoomInfoRepository chatRoomInfoRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PostRepository postRepository;

    Long existedSellerId;
    Long existedPostId;
    Long existedBuyerId;

    ChatRoom savedChatRoom;

    @BeforeEach
    void setUp() {
        dateSetUp();
    }

    @Test
    @DisplayName("방 정보가 존재하면 true, 존재하지 않으면 false를 반환한다.")
    void isExistedRoom_existedIdOrNot_returnTrueOrFalse() {
        //when_then
        assertThat(chatRoomInfoService.isExistedRoom(existedPostId, existedBuyerId)).isEqualTo(true);
        assertThat(chatRoomInfoService.isExistedRoom(existedPostId, existedSellerId)).isEqualTo(true);
        assertThat(chatRoomInfoService.isExistedRoom(existedPostId + 1, existedBuyerId)).isEqualTo(false);
    }

    void dateSetUp() {

        ChatRoom chatRoom = new ChatRoom();
        savedChatRoom = chatRoomRepository.save(chatRoom);

        existedBuyerId = 1L;
        existedSellerId = 2L;

        Category category = DataInitializerFactory.category();
        Category savedeCategory = categoryRepository.save(category);

        Post post = DataInitializerFactory.post(existedSellerId, savedeCategory);
        Post savedPost = postRepository.save(post);

        existedPostId = savedPost.getId();

        ChatRoomInfo buyderChatRoomInfo = DataInitializerFactory.buyerChatRoomInfo(existedPostId, existedBuyerId, savedChatRoom);
        ChatRoomInfo sellerChatRoomInfo = DataInitializerFactory.sellerChatRoomInfo(existedPostId, existedSellerId, savedChatRoom);

        chatRoomInfoRepository.save(buyderChatRoomInfo);
        chatRoomInfoRepository.save(sellerChatRoomInfo);
    }

}
