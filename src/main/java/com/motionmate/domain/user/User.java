package com.motionmate.domain.user;

import com.motionmate.domain.follow.Follow;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password; // 소셜은 null 가능
    private String nickname;
    private String profileImageUrl;
    private String provider; // google, kakao, naver

    @OneToMany(mappedBy="follower", fetch= FetchType.LAZY)
    private List<Follow> followers;

    @OneToMany(mappedBy="following", fetch= FetchType.LAZY)
    private List<Follow> followings;

    @Builder
    public User(String email, String nickname, String profileImageUrl, String provider) {
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider;
    }

    // 소셜 전용이면 비밀번호 관련 메서드 삭제해도 된다.
}
