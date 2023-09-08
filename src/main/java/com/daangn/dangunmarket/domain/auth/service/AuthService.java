package com.daangn.dangunmarket.domain.auth.service;

import com.daangn.dangunmarket.domain.auth.dto.AuthResponse;
import com.daangn.dangunmarket.domain.auth.jwt.AuthToken;
import com.daangn.dangunmarket.domain.auth.jwt.AuthTokenProvider;
import com.daangn.dangunmarket.domain.member.repository.MemberRepository;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    private final AuthTokenProvider authTokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;

    public AuthService(AuthTokenProvider authTokenProvider, MemberRepository memberRepository, RefreshTokenService refreshTokenService) {
        this.authTokenProvider = authTokenProvider;
        this.memberRepository = memberRepository;
        this.refreshTokenService = refreshTokenService;
    }

    public AuthResponse updateToken(AuthToken authToken) {

        if (authToken.isValidTokenClaims()) {
            Claims claims = authToken.getTokenClaims();
            String socialId = claims.getSubject();
            Long memberId = memberRepository.findBySocialId(socialId).getId();

            if (refreshTokenService.isExpiredRefreshToken(authToken.getToken())) {
                authToken = refreshTokenService.saveNewAccessTokenInfo(memberId, socialId, authToken.getToken());
            }
        }

        return AuthResponse.builder()
                .appToken(authToken.getToken())
                .build();
    }

}
