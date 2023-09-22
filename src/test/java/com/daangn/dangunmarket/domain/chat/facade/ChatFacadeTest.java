package com.daangn.dangunmarket.domain.chat.facade;

import com.daangn.dangunmarket.domain.DataInitializerFactory;
import com.daangn.dangunmarket.domain.chat.model.SessionInfo;
import com.daangn.dangunmarket.domain.chat.repository.SessionInfoRedisRepository;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.repository.MemberJpaRepository;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@ActiveProfiles("test")
class ChatFacadeTest {

    @Autowired
    private ChatFacade chatFacade;

    @Autowired
    private SessionInfoRedisRepository sessionInfoRedisRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    private Member savedMember;

    @BeforeEach
    void setUp(){
        Member member = DataInitializerFactory.member();
        savedMember = memberJpaRepository.save(member);
    }

    @AfterEach
    void tearDown(){
        sessionInfoRedisRepository.deleteAll();
    }

    @Test
    @DisplayName("미리 저장되어있는 memberId와 sessionId를 통해 SessionInfo생성 후 sessionId로 조회하여 값을 확인한다.")
    void saveSessionInfo_correctMemberIdAndSessionId_void() {
        //given
        String sessionId = "5rsuwmct";
        Long savedMemberId = savedMember.getId();

        //when
        chatFacade.saveSessionInfo(sessionId, savedMemberId);

        //then
        SessionInfo sessionInfo = sessionInfoRedisRepository.findById(sessionId).get();
        assertThat(sessionInfo.getMemberId()).isEqualTo(savedMemberId);
    }

    @Test
    @DisplayName("존재하지 않는memberId와 sessionId를 통해 saveSessionInfo메서드를 호출 후 EntityNotFoundException가 발생하는 것을 확인한다.")
    void saveSessionInfo_incorrectMemberIdAndSessionId_EntityNotFoundException() {
        //given
        String sessionId = "5rsuwmct";
        Long savedMemberId = savedMember.getId() + 5;

        //when
        Exception exception = catchException(() -> chatFacade.saveSessionInfo(sessionId, savedMemberId));

        //then
        assertThat(exception).isInstanceOf(EntityNotFoundException.class);
    }
}