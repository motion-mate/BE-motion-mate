package com.motionmate.mapper;

import com.motionmate.domain.follow.Follow;
import com.motionmate.domain.user.User;
import com.motionmate.dto.follow.FollowRequestDto;
import com.motionmate.dto.follow.FollowResponseDto;
import com.motionmate.dto.follow.IsFollowingDto;
import org.springframework.stereotype.Component;

@Component
public class FollowMapper {

    // Follow -> FollowResponseDto
    public static FollowResponseDto toDto(User user) {
        return FollowResponseDto.builder()
                .userId(user.getId())
                .nickname(user.getProfile().getNickname())
                .profileImageUrl(user.getProfile().getProfileImageUrl())
                .build();
    }

    // FollowRequestDto -> Follow
    public static Follow toEntity(FollowRequestDto dto, User fromUser, User toUser) {
        return Follow.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .build();
    }

    public static IsFollowingDto toIsFollowingDto(boolean isFollowing) {
        return IsFollowingDto.builder()
                .isFollowing(isFollowing)
                .build();
    }


}
