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

    private String oauthNickname;


    private String provider; // google, kakao, naver 등

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_id") // 또는 name 생략 가능
    private UserProfile profile;

    @OneToMany(mappedBy="fromUser")
    private List<Follow> followers;

    @OneToMany(mappedBy="toUser")
    private List<Follow> followings;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    @Builder
    public User(String email, String oauthNickname, String provider, UserProfile profile) {
        this.email = email;
        //this.password = password;
        this.oauthNickname = oauthNickname;
        this.provider = provider;
        this.profile = profile;
    }

    public void connectProfile(UserProfile profile) {
        this.profile = profile;
    }


}
