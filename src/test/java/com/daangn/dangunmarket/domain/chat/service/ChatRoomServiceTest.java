package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.domain.DataInitializerFactory;
import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import com.daangn.dangunmarket.domain.chat.model.ChatRoom;
import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.chat.model.SessionInfo;
import com.daangn.dangunmarket.domain.chat.repository.chatentry.ChatRoomEntryRedisRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatmessage.ChatMessageMongoRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroom.ChatRoomRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.ChatRoomInfoRepository;
import com.daangn.dangunmarket.domain.chat.repository.sessioninfo.SessionInfoRedisRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
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
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SessionInfoRedisRepository sessionInfoRedisRepository;

    @Autowired
    private ChatRoomEntryRedisRepository chatRoomEntryRedisRepository;

    private Member existedSeller;
    private Long existedPostId;
    private Member existedBuyer;

    private ChatRoom savedChatRoom1;
    private ChatRoom savedChatRoom2;

    @BeforeEach
    void setUp() {
        dataSetUp();
    }

    @AfterEach
    void tearDown() {
        chatMessageMongoRepository.deleteAll();
        sessionInfoRedisRepository.deleteAll();;
        chatRoomEntryRedisRepository.deleteAllWithPrefix();
    }

    @Test
    @DisplayName("채팅방을 생성하고 채팅방 아이디로 다시 찾았을 때 존재하는 채팅방을 반환하는지 확인한다.")
    void createChatRoom_Optional_returnIsPresent() {
        //given
        Long newBuyerId = existedBuyer.getId() + 5;
        ChatRoomCreateRequest chatRoomCreateRequest = new ChatRoomCreateRequest(existedPostId);

        //when
        Long chatRoomId = chatRoomService.createChatRoom(newBuyerId, existedSeller.getId() ,chatRoomCreateRequest);
        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(chatRoomId);

        //then
        assertThat(chatRoom.isPresent()).isEqualTo(true);
    }

    @Test
    @DisplayName("memberId를 통해 member가 속한 채팅방들의 메타정보들을 조회할 수 있다.")
    void findChatRoomsByMemberId_memberId_ChatRoomsFindResponses() {
        //when
        ChatRoomsFindResponses responses = chatRoomService.findChatRoomsByMemberId(
                existedSeller.getId(),
                PageRequest.of(0, 10)
        );

        //then
        List<ChatRoomsFindResponse> contents = responses.resposes().getContent();

        assertThat(contents.get(0).chatRoomId()).isEqualTo(savedChatRoom1.getId());
        assertThat(contents.get(0).latestMessage()).isEqualTo("방 1의 두번째 메세지");
        assertThat(contents.get(0).otherMemberName()).isEqualTo("hany");
        assertThat(contents.get(0).readOrNot()).isEqualTo(1);

        assertThat(contents.get(1).chatRoomId()).isEqualTo(savedChatRoom2.getId());
        assertThat(contents.get(1).latestMessage()).isEqualTo("방 2의 두번째 메세지");
        assertThat(contents.get(1).otherMemberName()).isEqualTo("mac");
        assertThat(contents.get(1).readOrNot()).isEqualTo(0);
    }

    @Test
    @DisplayName("chatRoomId와 memberId를 통해 Member와 ChatRoom의 매핑 테이블인 ChatRoomInfo에서 해당 매핑 정보를 삭제할 수 있다.")
    void deleteChatRoomByIdAndMemberId_correctRoomIdAndMemberId_void(){
        //when
        chatRoomService.deleteChatRoomByIdAndMemberId(
                savedChatRoom1.getId(),
                existedBuyer.getId());

        //then
        List<ChatRoomsFindResponse> contents = chatRoomService.findChatRoomsByMemberId(
                existedBuyer.getId(),
                PageRequest.of(0, 10)
        ).resposes().getContent();

        ChatRoom chatRoom = chatRoomRepository.findById(savedChatRoom1.getId()).get();

        assertThat(contents.size()).isEqualTo(0);
        assertThat(chatRoom.getId()).isEqualTo(savedChatRoom1.getId());
    }

    @Test
    @DisplayName("채팅방에서 두명의 유저가 모두 나갈 경우 해당 채팅방은 삭제된다.")
    void deleteChatRoomByIdAndMemberId_checkDeleteChatRoom(){
        //when
        chatRoomService.deleteChatRoomByIdAndMemberId(
                savedChatRoom1.getId(),
                existedBuyer.getId());

        chatRoomService.deleteChatRoomByIdAndMemberId(
                savedChatRoom1.getId(),
                existedSeller.getId());

        //then
        assertThatThrownBy(() ->
                chatRoomRepository.findById(savedChatRoom1.getId())
                        .orElseThrow(RuntimeException::new))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("레디스의 ChatRoomEntry의 value인 memberId를 sessionId를 통해 삭제한다.")
    void deleteChatRoomEntryInMemberId_sessionId_void(){
        //given
        String sellerSessionId = "5rsuwmct";
        Long sellerId = existedSeller.getId();
        Long chatRoomId = savedChatRoom1.getId();

        String buyerSessionId = "7rshwmct";
        Long buyerId = existedBuyer.getId();

        setUpDeleteChatRoomEntryInMemberIdData(sellerSessionId, sellerId, chatRoomId, buyerSessionId, buyerId);

        //when
        chatRoomService.deleteChatRoomEntryInMemberId(buyerSessionId);

        //then
        Set<String> membersInRoom = chatRoomEntryRedisRepository.getMembersInRoom(chatRoomId.toString());
        assertThat(membersInRoom.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("레디스의 ChatRoomEntry에서 두명의 memberId가 삭제될 시 ChatRoomEntry가 삭제된다.")
    void deleteChatRoomEntryInMemberId_testDeleteEntry(){
        //given
        String sellerSessionId = "5rsuwmct";
        Long sellerId = existedSeller.getId();
        Long chatRoomId = savedChatRoom1.getId();

        String buyerSessionId = "7rshwmct";
        Long buyerId = existedBuyer.getId();

        setUpDeleteChatRoomEntryInMemberIdData(sellerSessionId, sellerId, chatRoomId, buyerSessionId, buyerId);

        //when
        chatRoomService.deleteChatRoomEntryInMemberId(buyerSessionId);
        chatRoomService.deleteChatRoomEntryInMemberId(sellerSessionId);

        //then
        Set<String> membersInRoom = chatRoomEntryRedisRepository.getMembersInRoom(chatRoomId.toString());
        assertThat(membersInRoom.size()).isEqualTo(0);
    }

    /**
     *  deleteChatRoomEntryInMemberId() 메서드 테스트를 위한 데이터 셋업
     */
    private void setUpDeleteChatRoomEntryInMemberIdData(String sellerSessionId, Long sellerId, Long chatRoomId, String buyerSessionId, Long buyerId) {
        sessionInfoRedisRepository.save(new SessionInfo(
                sellerSessionId,
                sellerId,
                chatRoomId
        ));

        sessionInfoRedisRepository.save(new SessionInfo(
                buyerSessionId,
                buyerId,
                chatRoomId
        ));

        chatRoomEntryRedisRepository.addMemberToRoom(chatRoomId.toString(), sellerId.toString());
        chatRoomEntryRedisRepository.addMemberToRoom(chatRoomId.toString(), buyerId.toString());
    }

    /**
     * 방     :   해당 방에 참여중인 유저        :  메세지를 보낸 순서
     * room1  : existedSeller, existedBuyer   :   existedSeller, existedBuyer
     * room2  : existedSeller, savedMember3   :   Member3, existedSeller
     */
    void dataSetUp() {
        savedChatRoom1 = chatRoomRepository.save(new ChatRoom());
        savedChatRoom2 = chatRoomRepository.save(new ChatRoom());

        existedSeller = memberJpaRepository.save(
                DataInitializerFactory.member("james", 35)
        );

        existedBuyer = memberJpaRepository.save(
                DataInitializerFactory.member("hany", 25)
        );

        Member savedMember3 = memberJpaRepository.save(
                DataInitializerFactory.member("mac", 27)
        );

        Category category = DataInitializerFactory.category();
        Category savedeCategory = categoryRepository.save(category);

        Post savedPost = postRepository.save(
                DataInitializerFactory.post(existedSeller.getId(), savedeCategory)
        );

        existedPostId = savedPost.getId();

        chatRoomInfoRepository.save(new ChatRoomInfo(
                true,
                existedPostId,
                savedChatRoom1,
                existedSeller.getId()
        ));

        chatRoomInfoRepository.save(new ChatRoomInfo(
                false,
                existedPostId,
                savedChatRoom1,
                existedBuyer.getId()
        ));

        chatRoomInfoRepository.save(new ChatRoomInfo(
                true,
                existedPostId,
                savedChatRoom2,
                existedSeller.getId()
        ));

        chatRoomInfoRepository.save(new ChatRoomInfo(
                false,
                existedPostId,
                savedChatRoom2,
                savedMember3.getId()
        ));

        chatMessageMongoRepository.save(new ChatMessage(
                savedChatRoom1.getId(),
                existedSeller.getId(),
                "방 1의 첫번째 메세지",
                "a",
                1
        ));

        chatMessageMongoRepository.save(new ChatMessage(
                savedChatRoom1.getId(),
                existedBuyer.getId(),
                "방 1의 두번째 메세지",
                "b",
                1
        ));

        chatMessageMongoRepository.save(new ChatMessage(
                savedChatRoom2.getId(),
                savedMember3.getId(),
                "방 2의 첫번째 메세지",
                "a",
                1
        ));

        chatMessageMongoRepository.save(new ChatMessage(
                savedChatRoom2.getId(),
                existedSeller.getId(),
                "방 2의 두번째 메세지",
                "a",
                1
        ));
    }

}
