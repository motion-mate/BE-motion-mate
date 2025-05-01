package com.motionmate.mapper;

import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserProfile;
import com.motionmate.dto.user.UserProfileDto;
import com.motionmate.dto.user.UserProfileResponseDto;
import com.motionmate.dto.user.UserProfileUpdateRequestDto;

public class         UserProfileMapper {

    // 단독 프로필 조회 DTO 변환
    public static UserProfileResponseDto toResponseDto(UserProfile profile) {
        return UserProfileResponseDto.builder()
                .bio(profile.getBio())
                .goal(profile.getGoal())
                .birthDate(profile.getBirthDate())
                .build();
    }

    // 마이페이지 통합 응답 DTO 변환
    public static UserProfileDto toUserProfileDto(User user, UserProfile profile) {
        return UserProfileDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .bio(profile != null ? profile.getBio() : "") // ✅ null-safe 처리
                .goal(profile != null ? profile.getGoal() : "") // ✅ 필요시 기본값
                .birthDate(profile != null ? profile.getBirthDate() : null) // ✅ 날짜는 null 허용
                .build();
    }


    // 요청 DTO → 기존 엔티티에 업데이트
    public static void updateFromDto(UserProfile profile, UserProfileUpdateRequestDto dto) {
        profile.updateProfile(dto.getBio(), dto.getGoal(), dto.getBirthDate());
    }
}
