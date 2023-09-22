package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.domain.chat.model.SessionInfo;
import com.daangn.dangunmarket.domain.chat.repository.SessionInfoRedisRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final SessionInfoRedisRepository sessionInfoRedisRepository;

    public ChatService(SessionInfoRedisRepository sessionInfoRedisRepository) {
        this.sessionInfoRedisRepository = sessionInfoRedisRepository;
    }

    public void saveSessionInfo(String sessionId, Long memberId) {
        SessionInfo sessionInfo = new SessionInfo(sessionId, memberId);
        sessionInfoRedisRepository.save(sessionInfo);
    }

}
