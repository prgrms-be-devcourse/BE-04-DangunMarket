package com.daangn.dangunmarket.domain.auth.client;

import com.daangn.dangunmarket.domain.auth.dto.GoogleUserResponse;
import com.daangn.dangunmarket.domain.auth.exception.TokenValidFailedException;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.model.MemberProvider;
import com.daangn.dangunmarket.domain.member.model.RoleType;
import com.daangn.dangunmarket.global.response.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ClientGoogle implements ClientProxy{

    private final WebClient webClient;

    public ClientGoogle(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Member getUserData(String accessToken) {

        GoogleUserResponse googleUserResponse = webClient.get()
                .uri("https://www.googleapis.com/oauth2/v3/userinfo")
                .headers(h -> h.set("Authorization",accessToken))
                .retrieve()
                .onStatus(status-> status.is4xxClientError(), response -> Mono.error(new TokenValidFailedException(ErrorCode.UNAUTHORIZED_TOKEN)))
                .onStatus(status-> status.is5xxServerError(), response -> Mono.error(new TokenValidFailedException(ErrorCode.OAUTH_CLIENT_SERVER_ERROR)))
                .bodyToMono(GoogleUserResponse.class)
                .block();

        System.out.println("googleUserResponse:"+googleUserResponse);

        return Member.builder()
                .socialId(googleUserResponse.getSub())
                .nickName(googleUserResponse.getName())
                .memberProvider(MemberProvider.GOOGLE)
                .roleType(RoleType.USER)
                .build();
    }
}
