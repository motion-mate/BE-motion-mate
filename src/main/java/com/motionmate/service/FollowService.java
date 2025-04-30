package com.motionmate.service;

import com.motionmate.domain.follow.FollowRepository;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.dto.follow.FollowResponseDto;
import com.motionmate.global.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
@Getter
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 팔로우
    public void follow(Long fromUserId, Long toUserId) {
        // 자기자신 팔로우 방지
    }

    // 언팔로우
    public void unfollow(Long userId, Long toUserId) {
    }

    // 내가 팔로우한 유저 목록
    public List<FollowResponseDto> getFollowings(Long userId) {
        return List.of();
    }

    // 나를 팔로우한 유저 목록
    public List<FollowResponseDto> getFollowers(Long userId) {
        return List.of();
    }

    // 팔로우 중복체크
    public boolean isFollowing(Long userId, Long toUserId) {
        return false;
    }
}
