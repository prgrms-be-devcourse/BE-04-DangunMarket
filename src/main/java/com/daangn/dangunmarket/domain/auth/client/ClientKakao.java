package com.daangn.dangunmarket.domain.auth.client;

import com.daangn.dangunmarket.domain.auth.dto.KakaoUserResponse;
import com.daangn.dangunmarket.domain.auth.exception.TokenValidFailedException;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.model.MemberProvider;
import com.daangn.dangunmarket.domain.member.model.RoleType;
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
                .onStatus(status-> status.is4xxClientError(), response -> Mono.error(new TokenValidFailedException("Social Access Token is unauthorized")))
                .onStatus(status-> status.is5xxServerError(), response -> Mono.error(new TokenValidFailedException("Internal Server Error")))
                .bodyToMono(KakaoUserResponse.class)
                .block();

        return Member.builder()
                .socialToken(String.valueOf(kakaoUserResponse.getId()))
                .nickName(kakaoUserResponse.getProperties().getNickname())
                .memberProvider(MemberProvider.KAKAO)
                .role(RoleType.USER)
                .build();
    }
}
