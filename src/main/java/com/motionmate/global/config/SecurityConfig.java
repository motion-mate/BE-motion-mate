package com.motionmate.global.config;

import com.motionmate.domain.user.UserRepository;
import com.motionmate.global.jwt.JwtAuthenticationFilter;
import com.motionmate.global.jwt.JwtTokenProvider;
import com.motionmate.global.oauth.CustomOAuth2UserService;
import com.motionmate.global.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.motionmate.global.oauth.JwtLogoutSuccessHandler;
import com.motionmate.global.oauth.OAuth2AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults()) // CORS 따로 WebConfig에서 설정한다면 disable
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).exceptionHandling(exception ->
                        exception.authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        })
                )

                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userRepository), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // ✅ 공개 허용 경로 (GET만 허용)
                        .requestMatchers(HttpMethod.POST, "/api/logout").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/reissue").permitAll()
                        .requestMatchers("/ws-stomp/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/feeds/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/goods/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/events/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/chatrooms/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/banners/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/exercises/list").permitAll()
                        .requestMatchers("/redis-test/**").permitAll()
                        // ✅ 관리자 전용 경로
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")



                        // ✅ 나머지는 전부 인증 필요
                        .anyRequest().authenticated()
                )

                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(auth -> auth
                                .authorizationRequestRepository(authorizationRequestRepository())
                        )

                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                );

        return http.build();
    }
}