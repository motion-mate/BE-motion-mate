package com.motionmate.controller.token;

import com.motionmate.domain.token.RefreshToken;
import com.motionmate.domain.token.RefreshTokenRepository;
import com.motionmate.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TokenController {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

        RefreshToken stored = refreshTokenRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No refresh token"));

        if (!stored.getToken().equals(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token mismatch");
        }

        // access token 재발급
        String newAccessToken = jwtTokenProvider.generateToken(userId);

// ✅ refresh token도 함께 갱신
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId); // 새 리프레시 토큰 생성
        stored.updateToken(newRefreshToken); // DB 값 갱신
        refreshTokenRepository.save(stored); // 저장

// ✅ 쿠키 설정
        String tokenCookie = String.format(
                "token=%s; Max-Age=%d; Path=/; HttpOnly; Secure=false; SameSite=Strict",
                newAccessToken, 60 * 30
        );
        String refreshTokenCookie = String.format(
                "refreshToken=%s; Max-Age=%d; Path=/; HttpOnly; Secure=false; SameSite=Strict",
                newRefreshToken, 60 * 60 * 24 * 7
        );

        return ResponseEntity.ok()
                .header("Set-Cookie", tokenCookie)
                .header("Set-Cookie", refreshTokenCookie)
                .build();
    }
}
