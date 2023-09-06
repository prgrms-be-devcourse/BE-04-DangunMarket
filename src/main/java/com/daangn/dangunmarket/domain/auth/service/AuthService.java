package com.daangn.dangunmarket.domain.auth.service;

import com.daangn.dangunmarket.domain.auth.dto.AuthResponse;
import com.daangn.dangunmarket.domain.auth.jwt.AuthToken;
import com.daangn.dangunmarket.domain.auth.jwt.AuthTokenProvider;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.repository.MemberRepository;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class AuthService {

    private final AuthTokenProvider authTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(AuthTokenProvider authTokenProvider, MemberRepository memberRepository) {
        this.authTokenProvider = authTokenProvider;
        this.memberRepository = memberRepository;
    }

    public AuthResponse updateToken(AuthToken authToken) {
        Claims claims = authToken.getTokenClaims();
        if (claims == null) {
            return null;
        }

        String socialId = claims.getSubject();
        Long memberId = memberRepository.findBySocialToken(socialId).getId();

        AuthToken newAppToken = authTokenProvider.createUserAppToken(socialId, memberId);

        return AuthResponse.builder()
                .appToken(newAppToken.getToken())
                .build();
    }

    public Long getMemberId(String token) {
        AuthToken authToken = authTokenProvider.convertAuthToken(token);

        Claims claims = authToken.getTokenClaims();
        if (claims == null) {
            return null;
        }

        try {
            Member member = memberRepository.findBySocialToken(claims.getSubject());
            return member.getId();

        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다.");
        }
    }

}
