package com.motionmate.dto.follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class FollowRequestDto {

    private Long followingId;
}
