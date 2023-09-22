package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.domain.DataInitializerFactory;
import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import com.daangn.dangunmarket.domain.chat.model.ChatRoom;
import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.chat.repository.chatmessage.ChatMessageMongoRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroom.ChatRoomRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.ChatRoomInfoRepository;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomCreateRequest;
import com.daangn.dangunmarket.domain.member.repository.MemberRepository;
import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.repository.category.CategoryRepository;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestPropertySource(value = "classpath:/application-test.yml")
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

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ChatMessageService chatMessageService;

    @Autowired
    ChatMessageMongoRepository chatMessageRepository;

    Long existedSellerId;
    Long existedPostId;
    Long existedBuyerId;

    ChatRoom savedChatRoom;

    @BeforeEach
    void setUp() {
        chatMessageRepository.deleteAll();
        dateSetUp();

    }

    @AfterEach
    void tearDown() {
        chatMessageRepository.deleteAll();
    }

    @Test
    @DisplayName("채팅방을 생성하고 채팅방 아이디로 다시 찾았을 때 존재하는 채팅방을 반환하는지 확인한다.")
    void createChatRoom_Optional_returnIsPresent() {
        //given
        Long newBuyerId = existedBuyerId + 1;
        ChatRoomCreateRequest chatRoomCreateRequest = new ChatRoomCreateRequest(existedPostId, newBuyerId);

        //when
        Long chatRoomId = chatRoomService.createChatRoom(existedSellerId, chatRoomCreateRequest);
        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(chatRoomId);

        //then
        assertThat(chatRoom.isPresent()).isEqualTo(true);
    }

    @Test
    @DisplayName("입장하면 상대방이 보낸 읽지 않은 메세지가 모두 읽음 처리됨을 확인한다.")
    void readAllMessage_readNotMessage_returnReadMessageCount() {
        //given
        ChatMessage chatMessage1 = DataInitializerFactory.chatMessage1(savedChatRoom.getId(), existedSellerId);
        ChatMessage chatMessage2 = DataInitializerFactory.chatMessage2(savedChatRoom.getId(), existedSellerId);

        chatMessageRepository.save(chatMessage1);
        chatMessageRepository.save(chatMessage2);

        //when
        int readMessageSize = chatRoomService.readAllMessage(savedChatRoom.getId(), existedSellerId);

        //then
        assertThat(readMessageSize).isEqualTo(2);
        assertThat(chatRoomService.readAllMessage(savedChatRoom.getId(), existedSellerId)).isEqualTo(0);
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
