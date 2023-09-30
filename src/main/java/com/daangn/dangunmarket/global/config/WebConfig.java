package com.daangn.dangunmarket.global.config;

import com.daangn.dangunmarket.global.MemberInfoResolver;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MemberInfoResolver memberInfoResolver;

    public WebConfig(MemberInfoResolver memberInfoResolver) {
        this.memberInfoResolver = memberInfoResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberInfoResolver);
    }
}
