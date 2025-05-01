package com.motionmate.dto.follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class IsFollowingDto {

    //  프론트 작업 시 true -> 팔로우버튼, false -> 팔로잉버튼
    private boolean isFollowing;
}
