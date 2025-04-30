package com.motionmate.mapper;

import com.motionmate.domain.user.User;
import com.motionmate.dto.user.UserResponseDto;

public class UserMapper {

    public static UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}