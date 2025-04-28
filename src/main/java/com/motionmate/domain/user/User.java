package com.motionmate.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password; // 소셜은 null 가능
    private String nickname;
    private String profileImageUrl;
    private String provider; // google, kakao, naver

    @Builder
    public User(String email, String nickname, String profileImageUrl, String provider) {
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider;
    }

    // 소셜 전용이면 비밀번호 관련 메서드 삭제해도 된다.
}
