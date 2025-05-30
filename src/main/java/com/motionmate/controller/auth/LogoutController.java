package com.motionmate.controller.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// LogoutController.java
@Slf4j
@RestController
@RequestMapping("/api")
public class LogoutController {

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        log.info("🔓 로그아웃 API 진입");

        ResponseCookie tokenCookie = ResponseCookie.from("token", "")
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

        response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, userIdCookie.toString());

        log.info("🧹 쿠키 제거 완료: {}", tokenCookie);
        return ResponseEntity.ok().build();
    }

}

