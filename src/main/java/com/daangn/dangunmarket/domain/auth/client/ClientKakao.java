package com.daangn.dangunmarket.domain.auth.client;

import com.daangn.dangunmarket.domain.auth.dto.KakaoUserResponse;
import com.daangn.dangunmarket.domain.auth.exception.TokenValidFailedException;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.model.MemberProvider;
import com.daangn.dangunmarket.domain.member.model.RoleType;
import com.daangn.dangunmarket.global.response.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ClientKakao implements ClientProxy{

    private final WebClient webClient;

    public ClientKakao(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Member getUserData(String accessToken) {

        KakaoUserResponse kakaoUserResponse = webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(h -> h.set("Authorization",accessToken))
                .retrieve()
                .onStatus(status-> status.is4xxClientError(), response -> Mono.error(new TokenValidFailedException(ErrorCode.UNAUTHORIZED_TOKEN)))
                .onStatus(status-> status.is5xxServerError(), response -> Mono.error(new TokenValidFailedException(ErrorCode.OAUTH_CLIENT_SERVER_ERROR)))
                .bodyToMono(KakaoUserResponse.class)
                .block();

        return Member.builder()
                .socialId(String.valueOf(kakaoUserResponse.getId()))
                .nickName(kakaoUserResponse.getProperties().getNickname())
                .memberProvider(MemberProvider.KAKAO)
                .roleType(RoleType.USER)
                .build();
    }

}
