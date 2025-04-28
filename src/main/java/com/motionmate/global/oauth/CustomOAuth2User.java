package com.motionmate.global.oauth;

import com.motionmate.domain.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User {

    private final User user;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // 우선 권한은 필요 없으니까 null 리턴
    }

    @Override
    public String getName() {
        return user.getEmail(); // 유저 email을 name으로 반환
    }

    public Long getUserId() {
        return user.getId();
    }
}
