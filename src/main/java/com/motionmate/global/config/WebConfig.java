package com.motionmate.global.config;

import com.motionmate.global.Intercepter.ProfileCompletionInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final ProfileCompletionInterceptor profileCompletionInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 요청에 대해
                .allowedOrigins("http://localhost:3000") // 프론트 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization") // 토큰 헤더 클라이언트에서 읽기 가능하게
                .allowCredentials(true)
                .maxAge(3600);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(profileCompletionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/profile/register", // 등록 페이지는 제외
                        "/api/auth/**",      // 로그인 관련 경로
                        "/api/logout", "/api/reissue",
                        "/css/**", "/js/**", "/images/**",
                        "/swagger-ui/**", "/v3/api-docs/**"
                );
    }
}