package com.daangn.dangunmarket.domain.chat.facade;

import com.daangn.dangunmarket.domain.DataInitializerFactory;
import com.daangn.dangunmarket.domain.chat.facade.dto.ChatRoomCheckInParamResponse;
import com.daangn.dangunmarket.domain.chat.model.ChatRoom;
import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.chat.repository.chatentry.ChatRoomEntryRedisRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatmessage.ChatMessageMongoRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroom.ChatRoomRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.ChatRoomInfoRepository;
import com.daangn.dangunmarket.domain.member.model.Member;
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
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ChatRoomFacadeTest {

    @Autowired
    ChatRoomFacade chatRoomFacade;

    @Autowired
    ChatRoomRepository chatRoomRepository;

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

    @Autowired
    ChatRoomEntryRedisRepository chatEntryRedisRepository;

    ChatRoom savedChatRoom;
    Member existedBuyer;
    Member existedSeller;
    Long existedBuyerId;
    Long existedSellerId;
    Long existedPostId;
    Post savedPost;

    @BeforeEach
    void setUp() {
        chatMessageRepository.deleteAll();
        chatEntryRedisRepository.deleteAllWithPrefix();
        dateSetUp();
    }

    @AfterEach
    void tearDown() {
        chatEntryRedisRepository.deleteAllWithPrefix();
        chatMessageRepository.deleteAll();
    }

    @Test
    @DisplayName("채팅방에 입장했을 때 상대방의 별명과 리뷰점수 그리고 채팅과 연관된 게시글 정보의 일부를 확인할 수 있다.")
    void checkInChatRoom_returnPostAndMemberInfo_isEquals() {
        //given
        Long senderId = chatRoomInfoRepository.findSenderIdByChatRoomInfoAndMyId(savedChatRoom.getId(), existedBuyerId);
        Member chatPartner = memberRepository.findById(senderId).get();

        //when
        ChatRoomCheckInParamResponse chatRoomCheckInParamResponse = chatRoomFacade.checkInChatRoom(savedChatRoom.getId(), existedBuyerId);

        //then
        assertThat(chatRoomCheckInParamResponse.postId()).isEqualTo(savedPost.getId());
        assertThat(chatRoomCheckInParamResponse.isOfferAllowed()).isEqualTo(savedPost.isOfferAllowed());
        assertThat(chatRoomCheckInParamResponse.price()).isEqualTo(savedPost.getPrice());
        assertThat(chatRoomCheckInParamResponse.title()).isEqualTo(savedPost.getTitle());
        assertThat(chatRoomCheckInParamResponse.productUrl()).isEqualTo(savedPost.extractSingleImage());
        assertThat(chatRoomCheckInParamResponse.tradeState()).isEqualTo(savedPost.getTradeStatus().toString());
        assertThat(chatRoomCheckInParamResponse.nickName()).isEqualTo(chatPartner.getNickName());
        assertThat(chatRoomCheckInParamResponse.reviewScore()).isEqualTo(chatPartner.getReviewScore());
    }

    void dateSetUp() {
        ChatRoom chatRoom = new ChatRoom();
        savedChatRoom = chatRoomRepository.save(chatRoom);

        Member sellerToSave = DataInitializerFactory.member();
        Member buyerToSave = DataInitializerFactory.member2();

        existedSeller = memberRepository.save(sellerToSave);
        existedBuyer = memberRepository.save(buyerToSave);

        existedBuyerId = existedBuyer.getId();
        existedSellerId = existedSeller.getId();

        Category category = DataInitializerFactory.category();
        Category savedeCategory = categoryRepository.save(category);

        Post post = DataInitializerFactory.post(existedSellerId, savedeCategory);
        savedPost = postRepository.save(post);

        existedPostId = savedPost.getId();

        ChatRoomInfo buyderChatRoomInfo = DataInitializerFactory.buyerChatRoomInfo(existedPostId, existedBuyerId, savedChatRoom);
        ChatRoomInfo sellerChatRoomInfo = DataInitializerFactory.sellerChatRoomInfo(existedPostId, existedSellerId, savedChatRoom);

        chatRoomInfoRepository.save(buyderChatRoomInfo);
        chatRoomInfoRepository.save(sellerChatRoomInfo);
    }

}
