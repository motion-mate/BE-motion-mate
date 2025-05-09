package com.motionmate.global.oauth;

import com.motionmate.domain.user.UserRepository;
import com.motionmate.global.jwt.JwtTokenProvider;
import com.motionmate.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserService userService; // ✅ 추가

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("✅ OAuth2 로그인 성공");

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getUserId();

        // ✅ JWT 토큰 생성
        String token = jwtTokenProvider.generateToken(userId);

        // ✅ 닉네임 등록 여부 확인
        boolean isRegistered = userService.isProfileRegistered(userId);

        // ✅ 리다이렉트 URL 분기
        String redirectUrl;
        if (isRegistered) {
            redirectUrl = "http://localhost:3000/social/success?token=" + token + "&userId=" + userId;
        } else {
            redirectUrl = "http://localhost:3000/profile/register?token=" + token + "&userId=" + userId;
        }

        log.info("🔀 리다이렉트 URL: {}", redirectUrl); // ✅ 로그 추가
        response.sendRedirect(redirectUrl);
    }
}
