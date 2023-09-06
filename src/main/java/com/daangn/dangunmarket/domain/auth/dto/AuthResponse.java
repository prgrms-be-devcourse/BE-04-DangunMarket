package com.daangn.dangunmarket.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthResponse {

    private String appToken;
    private Boolean isNewMember;
    private Long userId;
    private String nickName;

    @Builder
    public AuthResponse(String appToken, Boolean isNewMember, Long userId, String nickName) {
        this.appToken = appToken;
        this.isNewMember = isNewMember;
        this.userId = userId;
        this.nickName = nickName;
    }

}
