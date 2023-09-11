package com.daangn.dangunmarket.domain.auth.config;

public class Constants {
    /**
     * 권한제외 대상
     * @see SecurityConfig
     */
    public static final String[] permitAllArray = new String[] {
            "/",
            "/auth/**"
    };

    /**
     *  client -> oauth provider ->(token~~ ) client -> backend -> client
     *  /api/auth/login/google
     *
     *  {
     *    type : google,
     *    token : asdfjsdfiiqweoruqiwer.qweriuqweuifjeuijqf.qwerjqwerujiqwir
     *
     *  }
     *   -> googleServer (personal) -> backend (save) ->(jwt) client
     *
     *
     *  client -> backend (redirect) oauthProvider(kakao login) -> oauth provider ->(token) backend ->(token) oauth provider ->(personal) backend ->(we jwt token) client
     */
}
