package com.motionmate.global.jwt;

import com.motionmate.domain.user.UserRepository;
import com.motionmate.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

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

        if (!(request instanceof ServletServerHttpRequest servletRequest)) {
            System.out.println("❌ WebSocket 요청 타입 오류");
            return false;
        }

        HttpServletRequest httpRequest = servletRequest.getServletRequest();
        String token = extractTokenFromCookie(httpRequest, "token");

        if (token == null) {
            System.out.println("❌ WebSocket 연결 실패: 토큰 없음");
            return false;
        }

        if (!jwtTokenProvider.validateToken(token)) {
            System.out.println("❌ WebSocket 연결 실패: 유효하지 않은 토큰");
            return false;
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        return userRepository.findById(userId)
                .map(user -> {
                    attributes.put("user", user);
                    System.out.println("✅ WebSocket 연결 성공: " + user.getProfile().getNickname());
                    return true;
                })
                .orElseGet(() -> {
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

    private String extractTokenFromCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
