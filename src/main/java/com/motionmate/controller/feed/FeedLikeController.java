package com.motionmate.controller.feed;


import com.motionmate.dto.feed.LikeCountResponseDto;
import com.motionmate.dto.feed.LikedStatusResponseDto;
import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.service.feed.FeedLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class FeedLikeController {

    private final FeedLikeService likeService;

    //좋아요 토클
    @PostMapping("/like/{feedId}")
    public ResponseEntity<LikedStatusResponseDto> toggleLike(
            @PathVariable Long feedId,
            @AuthenticationPrincipal CustomOAuth2User user) {
        boolean liked = likeService.toggleLike(feedId, user.getUser());

        return ResponseEntity.ok(new LikedStatusResponseDto(liked));
    }

    //좋아요 수
    @GetMapping("/like/{feedId}/count")
    public ResponseEntity<LikeCountResponseDto> likeCount(@PathVariable Long feedId) {
        int likeCount = likeService.getLikeCount(feedId);
        return ResponseEntity.ok(new LikeCountResponseDto(likeCount));
    }

    //좋아요 눌렀는지 여부 확인
    @GetMapping("/like/{feedId}")
    public ResponseEntity<LikedStatusResponseDto> isLiked(
            @PathVariable Long feedId,
            @AuthenticationPrincipal CustomOAuth2User user) {

        boolean liked = likeService.isLiked(feedId, user.getUser());
        return ResponseEntity.ok(new LikedStatusResponseDto(liked));
    }
}
