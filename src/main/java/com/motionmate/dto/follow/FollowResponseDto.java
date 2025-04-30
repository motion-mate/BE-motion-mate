package com.motionmate.dto.follow;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FollowResponseDto {

    // User(pk)
    private Long userId;
    private String nickname;
    private String profileImageUrl;
}
