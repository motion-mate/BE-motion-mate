package com.motionmate.domain.user;

import com.motionmate.domain.follow.Follow;
import com.motionmate.domain.notification.Notification;
import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

    @Column(nullable = false)
    private String socialId; // ✅ 소셜 고유 ID (provider별 sub, id)

    // enum 정의
    public enum Role {
        USER, ADMIN
    }

    // User 클래스에 필드 추가
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(name = "following_count")
    private int followingCount = 0;

    @Column(name = "follower_count")
    private int followerCount = 0;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_id") // 또는 name 생략 가능
    private UserProfile profile;

    @OneToMany(mappedBy="fromUser")
    private Set<Follow> followers = new HashSet<>();

    @OneToMany(mappedBy="toUser")
    private Set<Follow> followings = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    @Builder
    public User(String email, String oauthNickname, String provider,String socialId, UserProfile profile, Role role) {
        this.email = email;
        //this.password = password;
        this.oauthNickname = oauthNickname;
        this.provider = provider;
        this.socialId = socialId;
        this.profile = profile;
        this.role = Role.USER;

    }

    public void connectProfile(UserProfile profile) {
        this.profile = profile;
    }

    public void incrementFollowingCount() {
        this.followingCount++;
    }

    public void decrementFollowingCount() {
        if(this.followingCount > 0) this.followingCount--;
    }

    public void incrementFollowerCount() {
        this.followerCount++;
    }

    public void decrementFollowerCount() {
        if(this.followerCount > 0) this.followerCount--;
    }
}

