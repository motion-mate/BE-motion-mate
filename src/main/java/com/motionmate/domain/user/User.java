package com.motionmate.domain.user;

import com.motionmate.domain.follow.Follow;
import com.motionmate.domain.notification.Notification;
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

    //private String password; // 소셜 로그인은 null 가능

    private String nickname;

    private String profileImageUrl;

    private String provider; // google, kakao, naver 등

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserProfile profile;

    @OneToMany(mappedBy="fromUser", fetch= FetchType.LAZY)
    private List<Follow> followers;

    @OneToMany(mappedBy="toUser", fetch= FetchType.LAZY)
    private List<Follow> followings;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

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
