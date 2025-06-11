package com.motionmate.mapper.follow;

import com.motionmate.domain.follow.Follow;
import com.motionmate.domain.follow.FollowRepository;
import com.motionmate.domain.user.User;
import com.motionmate.dto.follow.FollowResponseDto;
import com.motionmate.dto.follow.IsFollowingDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FollowMapper {

    // Follow -> FollowResponseDto
    public static FollowResponseDto toDto(User user, List<Long> followingIds) {
        boolean isFollowing = followingIds.contains(user.getId());
        return FollowResponseDto.builder()
                .userId(user.getId())
                .nickname(user.getProfile().getNickname())
                .profileImageUrl(user.getProfile().getProfileImageUrl())
                .isFollowing(isFollowing)
                .build();
    }

    // FollowRequestDto -> Follow
    public static Follow toEntity(User fromUser, User toUser) {
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
