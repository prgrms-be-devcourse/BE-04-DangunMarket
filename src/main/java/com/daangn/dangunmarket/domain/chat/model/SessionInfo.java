package com.daangn.dangunmarket.domain.chat.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "session_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SessionInfo {

    @Id
    private String sessionId;

    private Long memberId;

    private Long roomId;

    @Builder
    public SessionInfo(String sessionId, Long memberId, Long roomId) {
        this.sessionId = sessionId;
        this.memberId = memberId;
        this.roomId = roomId;
    }
}
