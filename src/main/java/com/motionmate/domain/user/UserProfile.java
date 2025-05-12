package com.motionmate.domain.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nickname; // 사용자 설정 닉네임 (중복 불가)
    private String profileImageUrl;


    private String bio;
    private String goal;
    private LocalDate birthDate;

    private int followerCount;
    private int followingCount;

    @Builder    
    public UserProfile(String bio, String goal, LocalDate birthDate, String profileImageUrl, int followerCount, int followingCount) {
        this.bio = bio;
        this.goal = goal;
        this.birthDate = birthDate;
        this.profileImageUrl = profileImageUrl;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
    }


    public void updateProfile(String nickname, String bio, String goal, LocalDate birthDate, String profileImageUrl) {
        this.nickname = nickname;
        this.bio = bio;
        this.goal = goal;
        this.birthDate = birthDate;
        this.profileImageUrl = profileImageUrl;
    }

//    public void updateNickname(String nickname) {
//        this.nickname = nickname;
//    }
//    public void updateProfileImage(String profileImageUrl) {
//        this.profileImageUrl = profileImageUrl;
//    }


}
