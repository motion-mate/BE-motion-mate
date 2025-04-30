package com.motionmate.global.config;

import com.motionmate.global.jwt.JwtHandshakeInterceptor;
import com.motionmate.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // WebSocket을 사용한다는 어노테이션
@RequiredArgsConstructor
public class ChatConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    // 클라이언트에서 websocket에 접속하는 endpoint 등록
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint( "/ws-stomp")
                .addInterceptors(new JwtHandshakeInterceptor(jwtTokenProvider))
                .setAllowedOriginPatterns("*")
                .withSockJS();

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.enableSimpleBroker("/sub");

        registry.setApplicationDestinationPrefixes("/pub");

    }

}
