package com.motionmate.dto.follow;

import com.motionmate.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class FollowResponseDto {

    // User(pk)
    private Long userId;
    private String nickname;
    private String profileImageUrl;
    private boolean isFollowing;
}
