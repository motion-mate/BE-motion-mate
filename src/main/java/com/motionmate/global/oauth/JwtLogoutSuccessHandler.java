package com.motionmate.global.oauth;

import jakarta.servlet.ServletException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        String tokenCookie = "token=; Max-Age=0; Path=/; HttpOnly; Secure; SameSite=Strict";

        String userIdCookie = "userId=; Max-Age=0; Path=/; Secure; SameSite=Strict";

        response.setHeader("Set-Cookie", tokenCookie);
        response.addHeader("Set-Cookie", userIdCookie);

        response.sendRedirect("http://localhost:3000/main");
    }
}
