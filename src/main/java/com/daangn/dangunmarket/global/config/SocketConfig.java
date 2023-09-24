package com.daangn.dangunmarket.global.config;

import com.daangn.dangunmarket.domain.chat.handler.ChatErrorHandler;
import com.daangn.dangunmarket.domain.chat.handler.ChatPreHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@EnableWebSocketMessageBroker
public class SocketConfig implements WebSocketMessageBrokerConfigurer {

    private final ChatPreHandler chatPreHandler;
    private final ChatErrorHandler chatErrorHandler;

    public SocketConfig(ChatPreHandler chatPreHandler, ChatErrorHandler chatErrorHandler) {
        this.chatPreHandler = chatPreHandler;
        this.chatErrorHandler = chatErrorHandler;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .setErrorHandler(chatErrorHandler)
                .addEndpoint("/ws/chat")
                .addInterceptors()
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메세지 구독 요청 url -> 메세지 받을 때
        registry.enableSimpleBroker("/sub");

        // 메세지 발행 요청 url -> 메세지 보낼 때
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(chatPreHandler);
    }

}
