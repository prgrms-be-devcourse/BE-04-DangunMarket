package com.daangn.dangunmarket.domain.auth.dto;

import com.daangn.dangunmarket.domain.member.model.NickName;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KakaoUserResponse {

    private Long id;
    private Properties properties;
    private KakaoAccount kakaoAccount;

    public KakaoUserResponse(Long id, Properties properties, KakaoAccount kakaoAccount) {
        this.id = id;
        this.properties = properties;
        this.kakaoAccount = kakaoAccount;
    }

    @Getter
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Properties {
        private NickName nickname;

        public Properties(NickName nickname) {
            this.nickname = nickname;
        }

    }

    @Getter
    @NoArgsConstructor
    public static class KakaoAccount {
        private String email;

        public KakaoAccount(String email) {
            this.email = email;
        }

    }

}
