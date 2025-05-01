package com.motionmate.controller;

import com.motionmate.dto.follow.FollowResponseDto;
import com.motionmate.dto.follow.IsFollowingDto;
import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.service.FollowService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follows")
@AllArgsConstructor
public class FollowController {

    private final FollowService followService;

    // 팔로우
    @PostMapping("/{toUserId}")
    public ResponseEntity<Void> follow(
            @PathVariable Long toUserId,
            @AuthenticationPrincipal CustomOAuth2User user) {
        followService.follow(user.getUserId(), toUserId);
        return ResponseEntity.ok().build();
    }

    // 언팔로우
    @DeleteMapping("/unfollow/{toUserId}")
    public ResponseEntity<Void> unFollow(
            @PathVariable Long toUserId,
            @AuthenticationPrincipal CustomOAuth2User user) {
        followService.unfollow(user.getUserId(), toUserId);
        return ResponseEntity.ok().build();
    }

    // 내가 팔로우 중인 유저들(following)
    @GetMapping("/followings/{userId}")
    public ResponseEntity<List<FollowResponseDto>> getFollowings(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowings(userId));
    }

    // 나를 팔로우한 유저들(follower)
    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<FollowResponseDto>> getFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowers(userId));
    }

    // 특정유저를 팔로우했는지 유무(중복체크)
    @GetMapping("/status/{toUserId}")
    public ResponseEntity<IsFollowingDto> isFollowing(
            @PathVariable Long toUserId,
            @AuthenticationPrincipal CustomOAuth2User user) {
        IsFollowingDto isFollowing = followService.isFollowing(user.getUserId(), toUserId);
        return ResponseEntity.ok(isFollowing);
    }
}
