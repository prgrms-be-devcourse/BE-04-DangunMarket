package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.domain.DataInitializerFactory;
import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import com.daangn.dangunmarket.domain.chat.model.ChatRoom;
import com.daangn.dangunmarket.domain.chat.repository.chatmessage.ChatMessageMongoRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroom.ChatRoomRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.ChatRoomInfoRepository;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomCreateRequest;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomsFindResponse;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomsFindResponses;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.repository.MemberJpaRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestPropertySource(value = "classpath:/application-test.yml")
class ChatRoomServiceTest {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private ChatMessageMongoRepository chatMessageMongoRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatRoomInfoRepository chatRoomInfoRepository;

    @Autowired
    private ChatMessageMongoRepository chatMessageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;

    private Member existedSeller;
    private Member existedBuyer1;
    private Member existedBuyer2;

    private ChatRoom savedChatRoom1;
    private ChatRoom savedChatRoom2;

    private Long existedPostId;

    private ChatMessage room1ChatMessage1;
    private ChatMessage room1ChatMessage2;
    private ChatMessage room2ChatMessage1;
    private ChatMessage room2ChatMessage2;

    @BeforeEach
    void setUp() {
        chatMessageRepository.deleteAll();
        dataSetUp();
    }

    @AfterEach
    void tearDown() {
        chatMessageRepository.deleteAll();
        chatMessageMongoRepository.deleteAll();
    }

    @Test
    @DisplayName("채팅방을 생성하고 채팅방 아이디로 다시 찾았을 때 존재하는 채팅방을 반환하는지 확인한다.")
    void createChatRoom_Optional_returnIsPresent() {
        //given
        Long newBuyerId = existedBuyer1.getId() + 1;
        ChatRoomCreateRequest chatRoomCreateRequest = new ChatRoomCreateRequest(existedPostId, newBuyerId);

        //when
        Long chatRoomId = chatRoomService.createChatRoom(existedSeller.getId(), chatRoomCreateRequest);
        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(chatRoomId);

        //then
        assertThat(chatRoom.isPresent()).isEqualTo(true);
    }

    @Test
    @DisplayName("입장하면 상대방이 보낸 읽지 않은 메세지가 모두 읽음 처리됨을 확인한다.")
    void readAllMessage_readNotMessage_returnReadMessageCount() {
        //when
        int readMessageSize = chatRoomService.readAllMessage(savedChatRoom1.getId(), existedSeller.getId());

        //then
        assertThat(readMessageSize).isEqualTo(1);
        assertThat(chatRoomService.readAllMessage(savedChatRoom1.getId(), existedSeller.getId())).isEqualTo(0);
    }

    @Test
    @DisplayName("member의 id로 해당 유저가 속한 채팅방들을 조회 후 응답값을 확인한다.")
    void findChatRoomsByMemberId_memberId_chatRoomsFindResponses() {
        //when
        ChatRoomsFindResponses responses = chatRoomService.findChatRoomsByMemberId(
                existedSeller.getId(),
                PageRequest.of(0, 10)
        );

        //then
        List<ChatRoomsFindResponse> contents = responses.responses().getContent();

        assertThat(contents.get(0).chatRoomId()).isEqualTo(savedChatRoom1.getId());
        assertThat(contents.get(0).latestMessage()).isEqualTo(room1ChatMessage2.getMessage());
        assertThat(contents.get(0).otherMemberName()).isEqualTo(existedBuyer1.getNickName());
        assertThat(contents.get(0).readOrNot()).isEqualTo(room1ChatMessage2.getReadOrNot());
    }

    @Test
    @DisplayName("member의 id로 해당 유저가 속한 채팅방들을 조회 후 최근 보낸 메세지가 본인이 보낸 메세지이면 읽음여부를 읽음으로 반환한다.")
    void findChatRoomsByMemberId_messageBySameMemberId_returnRoad() {
        //when
        ChatRoomsFindResponses responses = chatRoomService.findChatRoomsByMemberId(
                existedSeller.getId(),
                PageRequest.of(0, 10)
        );

        //then
        List<ChatRoomsFindResponse> contents = responses.responses().getContent();

        assertThat(contents.get(1).chatRoomId()).isEqualTo(savedChatRoom2.getId());
        assertThat(contents.get(1).latestMessage()).isEqualTo(room2ChatMessage2.getMessage());
        assertThat(contents.get(1).otherMemberName()).isEqualTo(existedBuyer2.getNickName());
        assertThat(contents.get(1).readOrNot()).isEqualTo(0);
    }

    @Test
    @DisplayName("채팅방에서 Member 1명을 제거 후 해당 MemberId로 채팅방이 조회되는지 확인하고, 아직 채팅방에 1명이 참여중이므로 chatRoom이 남아있는지 확인한다.")
    void deleteChatRoomByIdAndMemberId_correctRoomIdAndMemberId_void() {
        //when
        chatRoomService.deleteChatRoomByIdAndMemberId(
                savedChatRoom1.getId(),
                existedBuyer1.getId());

        //then
        List<ChatRoomsFindResponse> contents = chatRoomService.findChatRoomsByMemberId(
                existedBuyer1.getId(),
                PageRequest.of(0, 10)
        ).responses().getContent();

        ChatRoom chatRoom = chatRoomRepository.findById(savedChatRoom1.getId()).get();

        assertThat(contents.size()).isEqualTo(0);
        assertThat(chatRoom.getId()).isEqualTo(savedChatRoom1.getId());
    }

    @Test
    @DisplayName("채팅방에서 두명의 유저가 제거 되었을 때 해당 ChatRoom엔티티가 정상적으로 delete되는지 확인한다.")
    void deleteChatRoomByIdAndMemberId_checkDeleteChatRoom() {
        //when
        chatRoomService.deleteChatRoomByIdAndMemberId(
                savedChatRoom1.getId(),
                existedBuyer1.getId());

        chatRoomService.deleteChatRoomByIdAndMemberId(
                savedChatRoom1.getId(),
                existedSeller.getId());

        //then
        assertThatThrownBy(() ->
                chatRoomRepository.findById(savedChatRoom1.getId())
                        .orElseThrow(RuntimeException::new))
                .isInstanceOf(RuntimeException.class);
    }

    /**
     * 방     :   해당 방에 참여중인 유저        :  메세지를 보낸 순서
     * room1  : existedSeller, existedBuyer1   :   existedSeller, existedBuyer
     * room2  : existedSeller, existedBuyer2   :   existedBuyer2, existedSeller
     */
    void dataSetUp() {
        savedChatRoom1 = chatRoomRepository.save(new ChatRoom());
        savedChatRoom2 = chatRoomRepository.save(new ChatRoom());

        existedSeller = memberJpaRepository.save(DataInitializerFactory.member("james", 35));
        existedBuyer1 = memberJpaRepository.save(DataInitializerFactory.member("hany", 25));
        existedBuyer2 = memberJpaRepository.save(DataInitializerFactory.member("mac", 27));

        Category category = DataInitializerFactory.category();
        Category savedeCategory = categoryRepository.save(category);
        Post savedPost = postRepository.save(DataInitializerFactory.post(existedSeller.getId(), savedeCategory));
        existedPostId = savedPost.getId();

        chatRoomInfoRepository.saveAll(
                List.of(
                        DataInitializerFactory.sellerChatRoomInfo(existedPostId, existedSeller.getId(), savedChatRoom1),
                        DataInitializerFactory.buyerChatRoomInfo(existedPostId, existedBuyer1.getId(), savedChatRoom1),
                        DataInitializerFactory.sellerChatRoomInfo(existedPostId, existedSeller.getId(), savedChatRoom2),
                        DataInitializerFactory.buyerChatRoomInfo(existedPostId, existedBuyer2.getId(), savedChatRoom2)
                )
        );

        room1ChatMessage1 = DataInitializerFactory.chatMessage1(savedChatRoom1.getId(), existedSeller.getId());
        room1ChatMessage2 = DataInitializerFactory.chatMessage2(savedChatRoom1.getId(), existedBuyer1.getId());

        room2ChatMessage1 = DataInitializerFactory.chatMessage3(savedChatRoom2.getId(), existedBuyer2.getId());
        room2ChatMessage2 = DataInitializerFactory.chatMessage4(savedChatRoom2.getId(), existedSeller.getId());

        chatMessageRepository.save(room1ChatMessage1);
        chatMessageRepository.save(room1ChatMessage2);
        chatMessageRepository.save(room2ChatMessage1);
        chatMessageRepository.save(room2ChatMessage2);
    }

}
