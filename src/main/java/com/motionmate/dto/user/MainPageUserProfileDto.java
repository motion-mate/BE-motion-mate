package com.motionmate.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MainPageUserProfileDto {
    private Long userId;
    private String nickname;
    private String profileImageUrl;
    private String bio;
    private int followerCount;  // 팔로워 수
    private int followingCount; // 팔로잉 수
}
