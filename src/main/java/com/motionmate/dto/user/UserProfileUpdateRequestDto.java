package com.motionmate.dto.user;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserProfileUpdateRequestDto {
    private String nickname;
    private String bio;
    private String goal;
    private LocalDate birthDate;
    private String profileImageUrl;
    private String bucketKey;
    private String orgName;
    private int followerCount;
    private int followingCount;
}