package com.daangn.dangunmarket.domain.auth.service;

import com.daangn.dangunmarket.domain.auth.client.ClientProxy;
import com.daangn.dangunmarket.domain.auth.dto.AuthResponse;
import com.daangn.dangunmarket.domain.auth.jwt.AuthToken;
import com.daangn.dangunmarket.domain.auth.jwt.AuthTokenProvider;
import com.daangn.dangunmarket.domain.auth.client.ClientStrategy;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.repository.MemberRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClientService {

    private final ClientStrategy clientStrategy;
    private final AuthTokenProvider authTokenProvider;
    private final MemberRepository memberRepository;

    public ClientService(ClientStrategy clientStrategy, AuthTokenProvider authTokenProvider, MemberRepository memberRepository) {
        this.clientStrategy = clientStrategy;
        this.authTokenProvider = authTokenProvider;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public AuthResponse login(String client, String accessToken) {
        ClientProxy clientProxy = clientStrategy.getClient(client);

        Member clientMember = clientProxy.getUserData(accessToken);
        String socialId = clientMember.getSocialToken();

        Optional<Member> memberOptional = memberRepository.findBySocialTokenOptional(socialId);
        boolean isNewMember = !memberOptional.isPresent();

        Member savedMember = memberOptional.orElseGet(() -> memberRepository.save(clientMember));

        AuthToken appToken = authTokenProvider.createUserAppToken(socialId, savedMember.getId());

        return AuthResponse.builder()
                .appToken(appToken.getToken())
                .isNewMember(isNewMember)
                .userId(savedMember.getId())
                .nickName(savedMember.getNickName().getNickName())
                .build();
    }

}
