package com.daangn.dangunmarket.domain.auth.config;

public class Constants {
    /**
     * 권한제외 대상
     *
     * @see SecurityConfig
     */
    public static final String[] permitAllArray = new String[]{
            "/",
            "/auth/**",
            "/ws/chat"
    };

}
