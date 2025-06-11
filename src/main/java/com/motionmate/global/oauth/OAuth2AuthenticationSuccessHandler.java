package com.motionmate.global.oauth;

import com.motionmate.domain.token.RefreshToken;
import com.motionmate.domain.token.RefreshTokenRepository;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.global.jwt.JwtTokenProvider;
import com.motionmate.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService; // ✅ 추가
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("✅ OAuth2 로그인 성공");

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getUserId();

        // ✅ JWT 토큰 생성
        String token = jwtTokenProvider.generateToken(userId);

        // ✅ Refresh Token 생성
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId);

        // ✅ DB에 저장 또는 갱신
        refreshTokenRepository.save(new RefreshToken(userId, refreshToken));

// ✅ 닉네임 등록 여부 확인
        boolean isRegistered = userService.isProfileRegistered(userId);

// ✅ 리다이렉트 URL 분기
        String redirectUrl;
        if (isRegistered) {
            redirectUrl = "http://motionmate.store/social/success?userId=" + userId + "&loginSuccess=true";
        } else {
            redirectUrl = "http://motionmate.store/profile/register?userId=" + userId + "&loginSuccess=true";
        }

        log.info("🔀 리다이렉트 URL: {}", redirectUrl); // ✅ 로그 추가

        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(response);

// 개발환경(localhost)이라면 임시로 Secure, SameSite 조정
        String tokenCookie = String.format(
                "token=%s; Max-Age=%d; Path=/; HttpOnly; Secure=false; SameSite=Strict; Domain=52.79.118.166",
                token,
                60 * 60 * 24
        );

        // ✅ Refresh Token Cookie
        String refreshCookie = String.format(
                "refreshToken=%s; Max-Age=%d; Path=/; HttpOnly; Secure=false; SameSite=Lax; Domain=52.79.118.166", // 변경됨
                refreshToken,
                60 * 60 * 24 * 14
        );


        String userIdCookie = String.format(
                "userId=%d; Max-Age=%d; Path=/; Domain=52.79.118.166",
                userId,
                60 * 60 * 24
        );

        response.setHeader("Set-Cookie", tokenCookie);
        response.addHeader("Set-Cookie", refreshCookie);
        response.addHeader("Set-Cookie", userIdCookie);

        response.sendRedirect(redirectUrl);
    }





}
