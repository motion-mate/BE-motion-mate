package com.motionmate.mapper;

import com.motionmate.domain.user.User;
import com.motionmate.dto.follow.FollowCountResponse;
import com.motionmate.dto.follow.FollowResponseDto;
import com.motionmate.dto.user.UserResponseDto;

public class UserMapper {

    public static UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }

    public static FollowCountResponse toFollowCountDto(User user) {
        return FollowCountResponse.builder()
                .followingCount(user.getFollowingCount())
                .followerCount(user.getFollowerCount())
                .build();
    }
}