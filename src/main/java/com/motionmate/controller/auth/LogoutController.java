package com.motionmate.controller.auth;

import com.motionmate.domain.token.RefreshTokenRepository;
import com.motionmate.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LogoutController {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        log.info("🔓 로그아웃 API 진입");

        // ✅ 쿠키에서 refreshToken 추출
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        // ✅ refreshToken 유효하면 DB에서 삭제
        if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
            Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
            refreshTokenRepository.deleteById(userId);
            log.info("✅ refreshToken 삭제 완료: userId = {}", userId);
        } else {
            log.warn("⚠️ refreshToken 없음 또는 유효하지 않아 DB 삭제 안 됨");
        }

        // ✅ 쿠키 제거
        ResponseCookie tokenCookie = ResponseCookie.from("token", "")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(0)
                .sameSite("Lax")
                .domain("localhost")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(0)
                .sameSite("Lax")
                .domain("localhost")
                .build();

        ResponseCookie userIdCookie = ResponseCookie.from("userId", "")
                .path("/")
                .secure(false)
                .maxAge(0)
                .sameSite("Lax")
                .domain("localhost")
                .build();

        ResponseCookie jsessionidCookie = ResponseCookie.from("JSESSIONID", "")
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .secure(false)
                .domain("localhost")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, userIdCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, jsessionidCookie.toString());

        SecurityContextHolder.clearContext();
        log.info("🧹 쿠키 및 리프레시 토큰 제거 완료");

        return ResponseEntity.ok().build();
    }
}
