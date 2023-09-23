package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.domain.chat.model.SessionInfo;
import com.daangn.dangunmarket.domain.chat.repository.sessioninfo.SessionInfoRepository;
import com.daangn.dangunmarket.domain.chat.service.dto.SessionInfoSaveRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatService {

    private final SessionInfoRepository sessionInfoRepository;

    public ChatService(SessionInfoRepository sessionInfoRepository) {
        this.sessionInfoRepository = sessionInfoRepository;
    }

    @Transactional
    public void saveSessionInfo(SessionInfoSaveRequest request) {
        SessionInfo sessionInfo = SessionInfo.builder()
                .sessionId(request.sessionId())
                .memberId(request.memberId())
                .roomId(request.roomId())
                .build();

        sessionInfoRepository.save(sessionInfo);
    }

}
