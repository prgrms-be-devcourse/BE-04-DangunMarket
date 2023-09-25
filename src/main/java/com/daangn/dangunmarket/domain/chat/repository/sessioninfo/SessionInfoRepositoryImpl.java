package com.daangn.dangunmarket.domain.chat.repository.sessioninfo;

import com.daangn.dangunmarket.domain.chat.model.SessionInfo;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SessionInfoRepositoryImpl implements SessionInfoRepository{

    private final SessionInfoRedisRepository sessionInfoRedisRepository;

    public SessionInfoRepositoryImpl(SessionInfoRedisRepository sessionInfoRedisRepository) {
        this.sessionInfoRedisRepository = sessionInfoRedisRepository;
    }

    @Override
    public SessionInfo save(SessionInfo sessionInfo) {
        return sessionInfoRedisRepository.save(sessionInfo);
    }

    @Override
    public Optional<SessionInfo> findById(String sessionId) {
        return sessionInfoRedisRepository.findById(sessionId);
    }

    @Override
    public void delete(SessionInfo sessionInfo) {
        sessionInfoRedisRepository.delete(sessionInfo);
    }

}
