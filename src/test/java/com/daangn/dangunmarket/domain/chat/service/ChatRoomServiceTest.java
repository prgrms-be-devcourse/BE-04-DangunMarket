package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.domain.DataInitializerFactory;
import com.daangn.dangunmarket.domain.chat.exception.RoomNotCreateException;
import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import com.daangn.dangunmarket.domain.chat.model.ChatRoom;
import com.daangn.dangunmarket.domain.chat.model.SessionInfo;
import com.daangn.dangunmarket.domain.chat.repository.chatentry.ChatRoomEntryRedisRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatmessage.ChatMessageMongoRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroom.ChatRoomRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.ChatRoomInfoRepository;
import com.daangn.dangunmarket.domain.chat.service.dto.*;
import com.daangn.dangunmarket.domain.chat.service.mapper.ChatDtoMapper;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Autowired
    private ChatDtoMapper chatDtoMapper;

    @Autowired
    private SessionInfoRedisRepository sessionInfoRedisRepository;

    @Autowired
    private ChatRoomEntryRedisRepository chatRoomEntryRedisRepository;


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
        sessionInfoRedisRepository.deleteAll();;
        chatRoomEntryRedisRepository.deleteAllWithPrefix();
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

    @Test
    @DisplayName("참여한 채팅방 메세지의 내용들을 최신순으로 출력한다.")
    void findByChatRoomIdWithPagination_sortedAsc_returnMessages( ) {
        //given
        ChatMessagePageRequest chatMessageRequest = new ChatMessagePageRequest(savedChatRoom1.getId(), 1, 10);

        //when
        List<ChatMessagePageResponse> byChatRoomIdWithPagination = chatRoomService.findByChatRoomIdWithPagination(chatMessageRequest, existedSeller.getId());
        ChatMessagePageResponse chatMessagePageResponse1 = chatDtoMapper.toChatMessagePageResponse(room1ChatMessage1, existedSeller.getId());
        ChatMessagePageResponse chatMessagePageResponse2 = chatDtoMapper.toChatMessagePageResponse(room1ChatMessage2, existedSeller.getId());

        //then
        assertThat(byChatRoomIdWithPagination.get(0).chatRoomId()).isEqualTo(chatMessagePageResponse1.chatRoomId());
        assertThat(byChatRoomIdWithPagination.get(0).imgUrl()).isEqualTo(chatMessagePageResponse1.imgUrl());
        assertThat(byChatRoomIdWithPagination.get(0).isMine()).isEqualTo(chatMessagePageResponse1.isMine());
        assertThat(byChatRoomIdWithPagination.get(0).readOrNot()).isEqualTo(chatMessagePageResponse1.readOrNot());
        assertThat(byChatRoomIdWithPagination.get(0).message()).isEqualTo(chatMessagePageResponse1.message());

        assertThat(byChatRoomIdWithPagination.get(1).chatRoomId()).isEqualTo(chatMessagePageResponse2.chatRoomId());
        assertThat(byChatRoomIdWithPagination.get(1).imgUrl()).isEqualTo(chatMessagePageResponse2.imgUrl());
        assertThat(byChatRoomIdWithPagination.get(1).isMine()).isEqualTo(chatMessagePageResponse2.isMine());
        assertThat(byChatRoomIdWithPagination.get(1).readOrNot()).isEqualTo(chatMessagePageResponse2.readOrNot());
        assertThat(byChatRoomIdWithPagination.get(1).message()).isEqualTo(chatMessagePageResponse2.message());
    }

    @Test
    @DisplayName("참여하는 채팅방의 메세지는 내가 보낸 메세지는 true, 상대가 보낸 메세지는 false로 구분할 수 있다.")
    void findByChatRoomIdWithPagination_sameMember_isMineTrue() {
        //given
        ChatMessagePageRequest chatMessageRequest = new ChatMessagePageRequest(savedChatRoom1.getId(), 1, 10);

        //when
        List<ChatMessagePageResponse> byChatRoomIdWithPagination = chatRoomService.findByChatRoomIdWithPagination(chatMessageRequest, existedSeller.getId());

        //then
        assertThat(byChatRoomIdWithPagination.get(0).isMine()).isEqualTo(true);
        assertThat(byChatRoomIdWithPagination.get(1).isMine()).isEqualTo(false);
    }

    @Test
    @DisplayName("구매를 원하는 회원이 채팅방을 생성하려고 할 때 이미 존재하는 채팅방이면 예외를 던진다.")
    void isExistedRoom_existedIdOrNot_returnTrueOrFalse() {

        //when_then
        assertThatThrownBy(() -> chatRoomService.isExistedChatRoomByBuyer(existedPostId, existedBuyer2.getId())).isInstanceOf(RoomNotCreateException.class);
    }

    @DisplayName("레디스의 ChatRoomEntry의 value인 memberId를 sessionId를 통해 삭제한다.")
    void deleteChatRoomEntryInMemberId_sessionId_void(){
        //given
        String sellerSessionId = "5rsuwmct";
        Long sellerId = existedSeller.getId();
        Long chatRoomId = savedChatRoom1.getId();

        String buyerSessionId = "7rshwmct";
        Long buyerId = existedBuyer1.getId();

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
        Long buyerId = existedBuyer1.getId();

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

        room1ChatMessage1 = chatMessageRepository.save(
                DataInitializerFactory.chatMessage1(
                        savedChatRoom1.getId(),
                        existedSeller.getId())
        );
        room1ChatMessage2 = chatMessageRepository.save(
                DataInitializerFactory.chatMessage2(
                        savedChatRoom1.getId(),
                        existedBuyer1.getId())
        );

        room2ChatMessage1 = chatMessageRepository.save(
                DataInitializerFactory.chatMessage3(
                        savedChatRoom2.getId(),
                        existedBuyer2.getId())
        );
        room2ChatMessage2 = chatMessageRepository.save(
                DataInitializerFactory.chatMessage4(
                        savedChatRoom2.getId(),
                        existedSeller.getId())
        );
    }

}
