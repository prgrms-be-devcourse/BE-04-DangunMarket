package com.daangn.dangunmarket.domain.auth.client;

import com.daangn.dangunmarket.domain.auth.dto.GoogleUserResponse;
import com.daangn.dangunmarket.domain.auth.exception.TokenValidFailedException;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.model.MemberProvider;
import com.daangn.dangunmarket.domain.member.model.RoleType;
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
                .onStatus(status-> status.is4xxClientError(), response -> Mono.error(new TokenValidFailedException("Social Access Token is unauthorized")))
                .onStatus(status-> status.is5xxServerError(), response -> Mono.error(new TokenValidFailedException("Internal Server Error")))
                .bodyToMono(GoogleUserResponse.class)
                .block();

        System.out.println("googleUserResponse:"+googleUserResponse);

        return Member.builder()
                .socialToken(googleUserResponse.getSub())
                .nickName(googleUserResponse.getName())
                .memberProvider(MemberProvider.GOOGLE)
                .roleType(RoleType.USER)
                .build();
    }
}
