package com.daangn.dangunmarket.domain.chat.handler;

import com.daangn.dangunmarket.domain.auth.jwt.AuthToken;
import com.daangn.dangunmarket.domain.auth.jwt.AuthTokenProvider;
import com.daangn.dangunmarket.global.exception.UnauthorizedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Component
public class ChatPreHandler implements ChannelInterceptor {

    private static final String HEADER_PREFIX = "Bearer ";
    private static final String HEADER_NAME = "Authorization";
    private final AuthTokenProvider tokenProvider;

    public ChatPreHandler(AuthTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        String authorizationHeader = headerAccessor.getFirstNativeHeader(HEADER_NAME);

        StompCommand stompCommand = headerAccessor.getCommand();
        if (StompCommand.CONNECT == stompCommand || StompCommand.SEND == stompCommand) {

            //헤더 토큰
            if (authorizationHeader != null && authorizationHeader.startsWith(HEADER_PREFIX)) {
                String tokenStr = authorizationHeader.substring(HEADER_PREFIX.length());
                AuthToken token = tokenProvider.convertAuthToken(tokenStr);

                if (token.isValidTokenClaims()) {
                    Authentication authentication = tokenProvider.getAuthentication(token);
                    headerAccessor.setUser(authentication);
                } else {
                    throw new UnauthorizedException("유효한 Jwt토큰이 아닙니다.");
                }
            }
        }
        return message;
    }

}


