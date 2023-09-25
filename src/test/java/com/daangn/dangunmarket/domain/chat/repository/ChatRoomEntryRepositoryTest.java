package com.daangn.dangunmarket.domain.chat.repository;

import com.daangn.dangunmarket.domain.DataInitializerFactory;
import com.daangn.dangunmarket.domain.chat.model.ChatRoom;
import com.daangn.dangunmarket.domain.chat.repository.chatentry.ChatRoomEntryRedisRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatmessage.ChatMessageMongoRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroom.ChatRoomRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.ChatRoomInfoRepository;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.repository.MemberRepository;
import com.daangn.dangunmarket.domain.post.repository.category.CategoryRepository;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class ChatRoomEntryRepositoryTest {

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    ChatRoomEntryRedisRepository chatRoomEntryRedisRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ChatRoomInfoRepository chatRoomInfoRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ChatMessageMongoRepository chatMessageRepository;

    ChatRoom savedChatRoom;
    Member existedBuyer;
    Member existedSeller;
    Long existedBuyerId;
    Long existedSellerId;

    @BeforeEach
    void setUp() {
        chatRoomEntryRedisRepository.deleteAllWithPrefix();
        dateSetUP();
    }

    @AfterEach
    void tearDown() {
        chatRoomEntryRedisRepository.deleteAllWithPrefix();
    }

    @Test
    @DisplayName("채팅방 입장저보를 추가하여 이를 다시 검색했을 때 회원의 아이디와 채팅방 정보가 올바르게 저장되었는지 확인한다.")
    void addMemberToChat_returnSaneRoomIdAndMemberId() {
        //when
        chatRoomEntryRedisRepository.addMemberToRoom(savedChatRoom.getId().toString(), existedBuyerId.toString());
        Set<String> memberIds = chatRoomEntryRedisRepository.getMembersInRoom(savedChatRoom.getId().toString());

        //then
        assertThat(memberIds).contains(existedBuyerId.toString());
    }

    @Test
    @DisplayName("두 명의 회원이 채팅방에 여러번 입장했을 때 입장정보가 중복되지 않게 2명으로 저장된다.")
    void addMemberToChat_twoMemberSeveralTime_returnTwoSize() {
        //when
        chatRoomEntryRedisRepository.addMemberToRoom(savedChatRoom.getId().toString(), existedBuyerId.toString());
        chatRoomEntryRedisRepository.addMemberToRoom(savedChatRoom.getId().toString(), existedBuyerId.toString());

        chatRoomEntryRedisRepository.addMemberToRoom(savedChatRoom.getId().toString(), existedSellerId.toString());
        chatRoomEntryRedisRepository.addMemberToRoom(savedChatRoom.getId().toString(), existedSellerId.toString());

        Set<String> memberIds = chatRoomEntryRedisRepository.getMembersInRoom(savedChatRoom.getId().toString());

        //then
        assertThat(memberIds.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("채팅방 입장정보에서 회원을 제거하고 채팅방 입장정보를 검색했을 때 제거된 회원의 정보는 포함되어 있지 않다.")
    void removeMemberFromChat_removedMember_notContain() {
        //given
        chatRoomEntryRedisRepository.addMemberToRoom(savedChatRoom.getId().toString(), existedBuyerId.toString());
        chatRoomEntryRedisRepository.addMemberToRoom(savedChatRoom.getId().toString(), existedSellerId.toString());
        Set<String> memberIds = chatRoomEntryRedisRepository.getMembersInRoom(savedChatRoom.getId().toString());
        int size = memberIds.size();


        //when
        chatRoomEntryRedisRepository.removeMemberFromRoom(savedChatRoom.getId().toString(), existedBuyerId.toString());
        Set<String> removedMemberIds = chatRoomEntryRedisRepository.getMembersInRoom(savedChatRoom.getId().toString());

        //then
        assertThat(removedMemberIds.contains(existedBuyerId.toString())).isEqualTo(false);
        assertThat(removedMemberIds.size()).isEqualTo(size - 1);
    }

    @Test
    @DisplayName("채팅방 참여정보 존재하는 회원은 true, 존재하지 않는 회원은 false를 반환한다.")
    void isMemberInChat_existedOrNot_returnTrueOrFalse() {
        //given
        chatRoomEntryRedisRepository.addMemberToRoom(savedChatRoom.getId().toString(), existedBuyerId.toString());

        //when_then
        assertThat(chatRoomEntryRedisRepository.isMemberInRoom(savedChatRoom.getId().toString(), existedBuyerId.toString())).isEqualTo(true);
        assertThat(chatRoomEntryRedisRepository.isMemberInRoom(savedChatRoom.getId().toString(), existedSellerId.toString())).isEqualTo(false);

    }

    void dateSetUP() {
        ChatRoom chatRoom = new ChatRoom();
        savedChatRoom = chatRoomRepository.save(chatRoom);

        Member sellerToSave = DataInitializerFactory.member();
        Member buyerToSave = DataInitializerFactory.member2();

        existedSeller = memberRepository.save(sellerToSave);
        existedBuyer = memberRepository.save(buyerToSave);

        existedBuyerId = existedBuyer.getId();
        existedSellerId = existedSeller.getId();
    }

}

