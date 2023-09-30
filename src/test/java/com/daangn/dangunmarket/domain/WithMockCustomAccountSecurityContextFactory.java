package com.daangn.dangunmarket.domain;

import com.daangn.dangunmarket.domain.auth.jwt.CustomUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WithMockCustomAccountSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomAccount> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomAccount customOAuth2Account) {
        final SecurityContext context = SecurityContextHolder.createEmptyContext();

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("memberId", customOAuth2Account.memberId());
        attributes.put("socialId", customOAuth2Account.socialId());
        attributes.put("username", customOAuth2Account.username());

        final CustomUser customUser = CustomUser.builder()
                .memberId(customOAuth2Account.memberId())
                .socialId(customOAuth2Account.socialId())
                .authorities(null)
                .build();

        final Authentication token = new UsernamePasswordAuthenticationToken(customUser, "dd",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        context.setAuthentication(token);
        return context;
    }

}
