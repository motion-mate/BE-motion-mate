package com.motionmate.global.config;

import com.motionmate.domain.user.UserRepository;
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

    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    // 클라이언트에서 websocket에 접속하는 endpoint 등록
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint( "/ws-stomp")
                .addInterceptors(jwtHandshakeInterceptor)
                .setAllowedOrigins("http://localhost:3000");

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.enableSimpleBroker("/sub"); // 방 입장 경로

        registry.setApplicationDestinationPrefixes("/pub"); // 메시지 전송 경로

    }

}
