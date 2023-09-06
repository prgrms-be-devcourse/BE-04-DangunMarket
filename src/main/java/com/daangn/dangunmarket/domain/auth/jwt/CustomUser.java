package com.daangn.dangunmarket.domain.auth.jwt;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Builder
public record CustomUser(
        Long memberId,
        String socialId,
        List<? extends GrantedAuthority> authorities) { }
