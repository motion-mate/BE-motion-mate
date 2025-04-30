package com.motionmate.global.jwt;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

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

        if (token == null || !jwtTokenProvider.validateToken(token)) {
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
        // 예: 쿼리에서 token 가져오기
        String uri = request.getURI().toString();
        if (uri.contains("token=")) {
            return uri.substring(uri.indexOf("token=") + 6);
        }
        return null;
    }

}
