package com.motionmate.global.jwt;

import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserRepository;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public JwtHandshakeInterceptor(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        String token = extractQueryParam(request, "token");
        String roomId = extractQueryParam(request, "roomId");

        if (token == null || roomId == null) {
            System.out.println("❌ WebSocket 연결 실패: 토큰 또는 roomId 누락");
            return false;
        }

        if (!jwtTokenProvider.validateToken(token)) {
            System.out.println("❌ WebSocket 연결 실패: 유효하지 않은 토큰");
            return false;
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        return userRepository.findById(userId).map(user -> {
            attributes.put("user", user);       // 유저 전체 객체 저장
            attributes.put("roomId", roomId);   // roomId도 저장
            System.out.println("✅ WebSocket 연결 성공: " + user.getProfile().getNickname() + ", Room ID: " + roomId);
            return true;
        }).orElseGet(() -> {
            System.out.println("❌ WebSocket 연결 실패: 사용자 없음");
            return false;
        });
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
        // 생략 가능
    }

    private String extractQueryParam(ServerHttpRequest request, String param) {
        return UriComponentsBuilder.fromUri(request.getURI())
                .build()
                .getQueryParams()
                .getFirst(param);
    }
}
