package com.motionmate.domain.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    //private String password; // 소셜 로그인은 null 가능

    private String nickname;

    private String profileImageUrl;

    private String provider; // google, kakao, naver 등

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserProfile profile;

    @Builder
    public User(String email, String nickname, String profileImageUrl, String provider) {
        this.email = email;
        //this.password = password;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider;
    }

    public void connectProfile(UserProfile profile) {
        this.profile = profile;
        profile.setUser(this);
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
