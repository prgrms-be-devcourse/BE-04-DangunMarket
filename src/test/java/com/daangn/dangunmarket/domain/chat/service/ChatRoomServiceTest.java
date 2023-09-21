package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.domain.DataInitializerFactory;
import com.daangn.dangunmarket.domain.chat.model.ChatRoom;
import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.chat.repository.chatroom.ChatRoomRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.ChatRoomInfoRepository;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomCreateRequest;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ChatRoomServiceTest {

    @Autowired
    ChatRoomService chatRoomService;

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
    @DisplayName("채팅방을 생성하고 채팅방 아이디로 다시 찾았을 때 존재하는 채팅방을 반환하는지 확인한다.")
    void createChatRoom_Optional_returnIsPresent() {
        //given
        Long newBuyerId = existedBuyerId + 1;
        ChatRoomCreateRequest chatRoomCreateRequest = new ChatRoomCreateRequest(existedPostId);

        //when
        Long chatRoomId = chatRoomService.createChatRoom(newBuyerId, existedSellerId, chatRoomCreateRequest);
        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(chatRoomId);

        //then
        assertThat(chatRoom.isPresent()).isEqualTo(true);
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
