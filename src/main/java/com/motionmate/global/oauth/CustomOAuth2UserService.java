package com.motionmate.global.oauth;

import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserProfile;
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

        String provider = userRequest.getClientRegistration().getRegistrationId(); // google, kakao, naver
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = null;
        String nickname = null;
        String profileImageUrl = null;
        String socialId = null;

        if ("naver".equals(provider)) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            email = (String) response.get("email");
            nickname = (String) response.get("name");
            profileImageUrl = (String) response.get("profile_image");
            socialId = (String) response.get("id");
        } else if ("kakao".equals(provider)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            email = (String) kakaoAccount.get("email"); // 카카오는 null 가능
            nickname = (String) profile.get("nickname");
            profileImageUrl = (String) profile.get("profile_image_url");
            socialId = attributes.get("id").toString();
        } else { // google
            email = (String) attributes.get("email");
            nickname = (String) attributes.get("name");
            profileImageUrl = (String) attributes.get("picture");
            socialId = attributes.get("sub").toString();
        }

        final String fixedSocialId = socialId;
        final String fixedEmail = email;
        final String fixedNickname = nickname;
        final String fixedProvider = provider;

        User user = userRepository.findByProviderAndSocialId(provider, socialId)
                .orElseGet(() -> {
                    UserProfile profile = UserProfile.builder().build();
                    return userRepository.save(
                            User.builder()
                                    .email(fixedEmail)
                                    .oauthNickname(fixedNickname)
                                    .provider(fixedProvider)
                                    .socialId(fixedSocialId)
                                    .profile(profile)
                                    .role(User.Role.USER)
                                    .build()
                    );
                });

        return new CustomOAuth2User(user, attributes);
    }

}
