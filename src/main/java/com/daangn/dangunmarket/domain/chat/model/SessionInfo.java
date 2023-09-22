package com.daangn.dangunmarket.domain.chat.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "session_info")
public class SessionInfo {

    @Id
    private String sessionId;

    private Long memberId;

    public SessionInfo(String sessionId, Long memberId) {
        this.sessionId = sessionId;
        this.memberId = memberId;
    }
}
