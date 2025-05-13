package com.motionmate.global.jwt;

import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.global.oauth.CustomOAuth2User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            Long userId = jwtTokenProvider.getUserIdFromToken(token);
            User user = userRepository.findById(userId).orElse(null);

            if (user != null) {
                CustomOAuth2User customUser = new CustomOAuth2User(user, null);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(customUser, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                filterChain.doFilter(request, response); // ✅ 인증 성공 시 필터 계속 진행
                return;
            }
        }

        // ✅ 인증 실패 시 401 반환 (302 리다이렉트 방지)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Unauthorized - Invalid or missing token\"}");
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        if(request.getCookies() != null) {
            for(jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}


