package com.motionmate.global.oauth;

import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String nickname = (String) attributes.get("name"); // 구글은 name이 닉네임 느낌
        String profileImageUrl = (String) attributes.get("picture"); // 구글은 picture 필드에 프로필 이미지
        String provider = userRequest.getClientRegistration().getRegistrationId(); // google, kakao, naver

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(email)
                                .nickname(nickname)
                                .profileImageUrl(profileImageUrl)
                                .provider(provider)
                                .build()
                ));

        return oAuth2User;
    }
}
