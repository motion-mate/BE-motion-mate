package com.motionmate.global.Intercepter;

import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.global.oauth.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component
@RequiredArgsConstructor
public class ProfileCompletionInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 인증 객체 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return true;
        }

        CustomOAuth2User userDetails = (CustomOAuth2User) authentication.getPrincipal();
        User user = userDetails.getUser();

        if (!user.isRegistered()) {
            response.sendRedirect("/profile/register?userId=" + user.getId() + "&loginSuccess=true");
            return false;
        }


        
        return true;
    }
}
