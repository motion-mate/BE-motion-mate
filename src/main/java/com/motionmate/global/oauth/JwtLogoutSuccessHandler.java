package com.motionmate.global.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException {

        Cookie tokenCookie = new Cookie("token", null);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setSecure(false); // 🔥 배포 시 true
        tokenCookie.setPath("/");
        tokenCookie.setMaxAge(0);
        tokenCookie.setDomain("52.79.118.166"); // 🔥 이거 반드시 추가
        response.addCookie(tokenCookie);

        Cookie userIdCookie = new Cookie("userId", null);
        userIdCookie.setSecure(false);
        userIdCookie.setPath("/");
        userIdCookie.setMaxAge(0);
        userIdCookie.setDomain("52.79.118.166"); // 🔥 이것도!
        response.addCookie(userIdCookie);

        // ✅ 리다이렉트
        response.sendRedirect("http://52.79.118.166:3000/main");
    }
}
