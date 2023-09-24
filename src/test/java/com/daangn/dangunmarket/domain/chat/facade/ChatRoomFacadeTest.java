package com.daangn.dangunmarket.domain.chat.facade;

import com.daangn.dangunmarket.domain.DataInitializerFactory;
import com.daangn.dangunmarket.domain.chat.facade.dto.ChatRoomCheckInParamResponse;
import com.daangn.dangunmarket.domain.chat.facade.dto.SessionInfoSaveFacaRequest;
import com.daangn.dangunmarket.domain.chat.model.ChatRoom;
import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.chat.model.SessionInfo;
import com.daangn.dangunmarket.domain.chat.repository.chatentry.ChatRoomEntryRedisRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatmessage.ChatMessageMongoRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroom.ChatRoomRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.ChatRoomInfoRepository;
import com.daangn.dangunmarket.domain.chat.repository.sessioninfo.SessionInfoRedisRepository;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.repository.MemberRepository;
import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.repository.category.CategoryRepository;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import com.daangn.dangunmarket.global.exception.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ChatRoomFacadeTest {

    @Autowired
    private ChatRoomFacade chatRoomFacade;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ChatRoomInfoRepository chatRoomInfoRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatMessageMongoRepository chatMessageRepository;

    @Autowired
    private ChatRoomEntryRedisRepository chatEntryRedisRepository;
    
    @Autowired
    private SessionInfoRedisRepository sessionInfoRedisRepository;

    private ChatRoom savedChatRoom;
    private Member existedBuyer;
    private Member existedSeller;
    private Long existedBuyerId;
    private Long existedSellerId;
    private Long existedPostId;
    private Post savedPost;

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
        sessionInfoRedisRepository.deleteAll();
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

    @Test
    @DisplayName("sessionId, roomId, roomId를 통해 소켓 통신중에 이용할 SessionInfo를 생성할 수 있다.")
    void saveSessionInfo_correctMemberIdAndSessionId_void() {
        //given
        String sessionId = "5rsuwmct";
        Long roomId = 1L;
        Long savedMemberId = existedSeller.getId();

        chatRoomFacade.saveSessionInfo(new SessionInfoSaveFacaRequest(
                //when
                sessionId,
                roomId,
                savedMemberId
                ));

        //then
        SessionInfo sessionInfo = sessionInfoRedisRepository.findById(sessionId).get();
        assertThat(sessionInfo.getMemberId()).isEqualTo(savedMemberId);
        assertThat(sessionInfo.getRoomId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("존재하지 않는 memberId와 sessionId를 통해 SessionInfo를 생성하려 하면 예외가 발생한다.")
    void saveSessionInfo_incorrectMemberIdAndSessionId_EntityNotFoundException() {
        //given
        String sessionId = "5rsuwmct";
        Long roomId = 1L;
        Long savedMemberId = existedSeller.getId() + 5;

        //when
        Exception exception = catchException(() -> chatRoomFacade.saveSessionInfo(new SessionInfoSaveFacaRequest(
                sessionId,
                roomId,
                savedMemberId
        )));

        //then
        assertThat(exception).isInstanceOf(EntityNotFoundException.class);
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
