package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.domain.chat.model.ChatRoom;
import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.chat.repository.chatmessage.ChatMessageMongoRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroom.ChatRoomJpaRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroom.ChatRoomRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.ChatRoomInfoJpaRepository;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.model.NickName;
import com.daangn.dangunmarket.domain.member.repository.MemberJpaRepository;
import com.daangn.dangunmarket.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.daangn.dangunmarket.domain.member.model.MemberProvider.GOOGLE;
import static com.daangn.dangunmarket.domain.member.model.RoleType.USER;
import static org.junit.jupiter.api.Assertions.*;

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

    @BeforeEach
    void setup(){
        dataSetup();
    }

    @Test
    void findChatRoomsByMemberId() {

    }

    private void dataSetup() {
        Member setupMember1 = Member.builder()
                .roleType(USER)
                .memberProvider(GOOGLE)
                .socialId("member2 socialId")
                .nickName(new NickName("james"))
                .reviewScore(35)
                .build();

        Member setupMember2 = Member.builder()
                .roleType(USER)
                .memberProvider(GOOGLE)
                .socialId("member3 socialId")
                .nickName(new NickName("hany"))
                .reviewScore(25)
                .build();

        Member savedMember1 = memberJpaRepository.save(setupMember1);
        Member savedMember2 = memberJpaRepository.save(setupMember2);

        ChatRoom savedRoom1 = chatRoomJpaRepository.save(new ChatRoom());
        ChatRoom savedRoom2 = chatRoomJpaRepository.save(new ChatRoom());

        Long setupRoomInfoId1 = chatRoomInfoJpaRepository.save(new ChatRoomInfo(
                true,
                1L,
                savedRoom1,
                savedMember1.getId()
        )).getId();

        Long setupRoomInfoId2 = chatRoomInfoJpaRepository.save(new ChatRoomInfo(
                false,
                1L,
                savedRoom1,
                savedMember2.getId()
        )).getId();

        Long setupRoomInfoId3 = chatRoomInfoJpaRepository.save(new ChatRoomInfo(
                false,
                2L,
                savedRoom2,
                savedMember1.getId()
        )).getId();

        chatRoomInfoJpaRepository.save(new ChatRoomInfo(
                true,
                2L,
                savedRoom2,
                savedMember2.getId()
        ));


    }
}