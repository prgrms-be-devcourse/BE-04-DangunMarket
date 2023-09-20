package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.domain.DataInitializerFactory;
import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import com.daangn.dangunmarket.domain.chat.model.ChatRoom;
import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.chat.repository.chatmessage.ChatMessageMongoRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroom.ChatRoomJpaRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.ChatRoomInfoJpaRepository;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomsFindResponse;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomsFindResponses;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.model.NickName;
import com.daangn.dangunmarket.domain.member.repository.MemberJpaRepository;

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

import static com.daangn.dangunmarket.domain.member.model.MemberProvider.GOOGLE;
import static com.daangn.dangunmarket.domain.member.model.RoleType.USER;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private ChatRoomJpaRepository chatRoomJpaRepository;

    @Autowired
    private ChatRoomInfoJpaRepository chatRoomInfoJpaRepository;

    @Autowired
    private ChatMessageMongoRepository chatMessageMongoRepository;

    private Member savedMember1;
    private ChatRoom savedRoom1;
    private ChatRoom savedRoom2;

    @BeforeEach
    void setup() {
        dataSetup();
    }

    @AfterEach
    void tearDown() {
        chatMessageMongoRepository.deleteAll();
    }

    @Test
    @DisplayName("member1의 id로 해당 유저가 속한 채팅방들을 조회 후 응답값을 확인한다.")
    void findChatRoomsByMemberId_memberId_ChatRoomsFindResponses() {
        //when
        ChatRoomsFindResponses responses = chatService.findChatRoomsByMemberId(
                savedMember1.getId(),
                PageRequest.of(0, 10)
        );

        //then
        List<ChatRoomsFindResponse> contents = responses.resposes().getContent();

        assertThat(contents.get(0).chatRoomId()).isEqualTo(1L);
        assertThat(contents.get(0).latestMessage()).isEqualTo("방 1의 두번째 메세지");
        assertThat(contents.get(0).otherMemberName()).isEqualTo("hany");
        assertThat(contents.get(0).readOrNot()).isEqualTo(1);

        assertThat(contents.get(1).chatRoomId()).isEqualTo(2L);
        assertThat(contents.get(1).latestMessage()).isEqualTo("방 2의 두번째 메세지");
        assertThat(contents.get(1).otherMemberName()).isEqualTo("mac");
        assertThat(contents.get(1).readOrNot()).isEqualTo(0);
    }

    /**
     * 방     :   해당 방에 참여중인 유저        :  메세지를 보낸 순서
     * room1  : savedMember1, savedMember2   :   Member1, Member2
     * room2  : savedMember1, savedMember3   :   Member3, Member1
     */
    private void dataSetup() {
        savedMember1 = memberJpaRepository.save(
                DataInitializerFactory.member("james", 35)
        );

        Member savedMember2 = memberJpaRepository.save(
                DataInitializerFactory.member("hany", 25)
        );

        Member savedMember3 = memberJpaRepository.save(
                DataInitializerFactory.member("mac", 27)
        );

        savedRoom1 = chatRoomJpaRepository.save(new ChatRoom());
        savedRoom2 = chatRoomJpaRepository.save(new ChatRoom());

        chatRoomInfoJpaRepository.save(new ChatRoomInfo(
                true,
                1L,
                savedRoom1,
                savedMember1.getId()
        ));

        chatRoomInfoJpaRepository.save(new ChatRoomInfo(
                false,
                1L,
                savedRoom1,
                savedMember2.getId()
        ));

        chatRoomInfoJpaRepository.save(new ChatRoomInfo(
                false,
                2L,
                savedRoom2,
                savedMember1.getId()
        ));

        chatRoomInfoJpaRepository.save(new ChatRoomInfo(
                true,
                2L,
                savedRoom2,
                savedMember3.getId()
        ));

        chatMessageMongoRepository.save(new ChatMessage(
                savedRoom1.getId(),
                savedMember1.getId(),
                "방 1의 첫번째 메세지",
                "a",
                1
        ));

        chatMessageMongoRepository.save(new ChatMessage(
                savedRoom1.getId(),
                savedMember2.getId(),
                "방 1의 두번째 메세지",
                "b",
                1
        ));

        chatMessageMongoRepository.save(new ChatMessage(
                savedRoom2.getId(),
                savedMember3.getId(),
                "방 2의 첫번째 메세지",
                "a",
                1
        ));

        chatMessageMongoRepository.save(new ChatMessage(
                savedRoom2.getId(),
                savedMember1.getId(),
                "방 2의 두번째 메세지",
                "a",
                1
        ));
    }

}
