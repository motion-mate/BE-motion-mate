package com.motionmate.global.jwt;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtHandshakeInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        String token = extractTokenFromRequest(request);

        if (token == null) {
            System.out.println("❌ WebSocket 연결 실패: 토큰 없음");
            return false;
        }

        if (!jwtTokenProvider.validateToken(token)) {
            System.out.println("❌ WebSocket 연결 실패: 유효하지 않은 토큰");
            return false;
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        attributes.put("userId", userId);

        return true;

    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {

    }

    private String extractTokenFromRequest(ServerHttpRequest request) {
        return UriComponentsBuilder.fromUri(request.getURI())
                .build()
                .getQueryParams()
                .getFirst("token");
    }


}
