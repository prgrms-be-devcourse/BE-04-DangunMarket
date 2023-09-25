package com.daangn.dangunmarket.domain.chat.repository.sessioninfo;

import com.daangn.dangunmarket.domain.chat.model.SessionInfo;
import com.daangn.dangunmarket.global.exception.EntityNotFoundException;

import java.util.Optional;

import static com.daangn.dangunmarket.global.response.ErrorCode.NOT_FOUND_ENTITY;

public interface SessionInfoRepository {

    SessionInfo save(SessionInfo sessionInfo);

    default SessionInfo getById(String sessionId){
        return findById(sessionId).orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_ENTITY));
    }

    Optional<SessionInfo> findById(String sessionId);

    void delete(SessionInfo sessionInfo);
}
