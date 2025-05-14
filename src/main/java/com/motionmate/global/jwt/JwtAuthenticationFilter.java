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


            }
        }

        // ✅ 인증이 안됐더라도 필터는 항상 진행해야 한다
        filterChain.doFilter(request, response);
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


