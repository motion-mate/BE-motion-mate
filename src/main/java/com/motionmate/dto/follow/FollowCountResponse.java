package com.motionmate.dto.follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FollowCountResponse {

    private int followingCount;
    private int followerCount;
}
