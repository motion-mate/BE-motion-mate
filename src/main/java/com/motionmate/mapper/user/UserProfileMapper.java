package com.motionmate.mapper.user;

import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserProfile;
import com.motionmate.dto.user.*;

public class UserProfileMapper {

    // 메인페이지용 간단 프로필 DTO 변환
    public static MainPageUserProfileDto toMainPageUserProfileDto(User user) {

        return MainPageUserProfileDto.builder()
                .userId(user.getId())
                .nickname(user.getProfile().getNickname())
                .bio(user.getProfile().getBio())
                .profileImageUrl(user.getProfile().getProfileImageUrl())
                .build();
    }

    // 단독 프로필 조회 DTO 변환 
    public static UserProfileResponseDto toResponseDto(UserProfile profile) {
        return UserProfileResponseDto.builder()
                .bio(profile.getBio())
                .goal(profile.getGoal())
                .birthDate(profile.getBirthDate())
                .profileImageUrl(profile.getProfileImageUrl())
                .build();
    }

    // 마이페이지 통합 응답 DTO 변환
    public static UserProfileDto toUserProfileDto(User user, UserProfile profile) {
        int followerCount = user.getFollowers().size();
        int followingCount = user.getFollowings().size();

        return UserProfileDto.builder()
                .userId(user.getId())
                .nickname(profile.getNickname())
                .profileImageUrl(profile.getProfileImageUrl())
                .bio(profile != null ? profile.getBio() : "")
                .goal(profile != null ? profile.getGoal() : "")
                .birthDate(profile != null ? profile.getBirthDate() : null)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .role(user.getRole().name())
                .build();
    }


    // 요청 DTO → 기존 엔티티에 업데이트
    public static void updateFromDto(UserProfile profile, UserProfileUpdateRequestDto dto) {
        profile.updateProfile(dto.getNickname(), dto.getBio(), dto.getGoal(), dto.getBirthDate(), dto.getProfileImageUrl(), dto.getBucketKey());
    }

    public static void updateFromDto(UserProfile profile, UserProfileRegisterRequestDto dto) {
        profile.updateProfile(dto.getNickname(), dto.getBio(), dto.getGoal(), dto.getBirthDate(), dto.getProfileImageUrl(), dto.getBucketKey());
    }
}
