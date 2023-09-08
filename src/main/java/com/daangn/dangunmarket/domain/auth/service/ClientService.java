package com.daangn.dangunmarket.domain.auth.service;

import com.daangn.dangunmarket.domain.auth.client.ClientProxy;
import com.daangn.dangunmarket.domain.auth.dto.AuthResponse;
import com.daangn.dangunmarket.domain.auth.jwt.AuthToken;
import com.daangn.dangunmarket.domain.auth.jwt.AuthTokenProvider;
import com.daangn.dangunmarket.domain.auth.client.ClientStrategy;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.repository.MemberJpaRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClientService {

    private final ClientStrategy clientStrategy;
    private final AuthTokenProvider authTokenProvider;
    private final MemberJpaRepository memberJpaRepository;
    private final RefreshTokenService refreshTokenService;

    public ClientService(ClientStrategy clientStrategy, AuthTokenProvider authTokenProvider, MemberJpaRepository memberJpaRepository, RefreshTokenService refreshTokenService) {
        this.clientStrategy = clientStrategy;
        this.authTokenProvider = authTokenProvider;
        this.memberJpaRepository = memberJpaRepository;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public AuthResponse login(String client, String accessToken) {
        ClientProxy clientProxy = clientStrategy.getClient(client);

        Member clientMember = clientProxy.getUserData(accessToken);
        String socialId = clientMember.getSocialId();

        Optional<Member> memberOptional = memberJpaRepository.findMemberIfExisted(socialId);
        Member savedMember = memberOptional.orElseGet(() -> memberJpaRepository.save(clientMember));


        AuthToken newAuthToken = refreshTokenService.saveAccessTokenCache(savedMember.getId(),socialId);

        return AuthResponse.builder()
                .appToken(newAuthToken.getToken())
                .isNewMember(!memberOptional.isPresent())
                .userId(savedMember.getId())
                .nickName(savedMember.getNickName().getNickName())
                .build();
    }

}
