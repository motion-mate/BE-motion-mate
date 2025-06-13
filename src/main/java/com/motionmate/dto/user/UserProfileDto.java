package com.motionmate.dto.user;

import com.motionmate.domain.follow.Follow;
import com.motionmate.dto.follow.FollowResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class UserProfileDto {
    private Long userId;
    private String nickname;
    private String profileImageUrl;
    private String role;
    private boolean isRegistered;

    private String bio;
    private String goal;
    private LocalDate birthDate;

    private int followerCount;  // 팔로워 수 메인페이지 표시
    private int followingCount; // 팔로잉 수 메인페이지 표시

    private List<FollowResponseDto> followers;  // 마이페이지에서 팔로워 세부 정보
    private List<FollowResponseDto> following;  // 마이페이지에서 팔로잉 세부 정보


}