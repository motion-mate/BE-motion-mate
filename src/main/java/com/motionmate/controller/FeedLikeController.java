package com.motionmate.controller;


import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.service.FeedLikeService;
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
    @PostMapping("/{id}/like")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable(name = "id") Long feedId,
            @AuthenticationPrincipal CustomOAuth2User user) {
        boolean liked = likeService.toggleLike(feedId, user.getUserId());

        Map<String, Object> response = new HashMap<>();
        response.put("liked", liked);
        return ResponseEntity.ok(response);
    }

    //좋아요 수
    @GetMapping("/{id}/like/count")
    public ResponseEntity<Map<String, Object>> likeCount(
            @PathVariable(name = "id") Long feedId,
            @AuthenticationPrincipal CustomOAuth2User user) {
        int likeCount = likeService.getLikeCount(feedId);
        Map<String, Object> response = new HashMap<>();
        response.put("likeCount", likeCount);
        return ResponseEntity.ok(response);
    }

    //좋아요 눌렀는지 여부 확인
    @GetMapping("/{id}/like")
    public ResponseEntity<Map<String, Object>> isLiked(
            @PathVariable(name = "id") Long feedId,
            @AuthenticationPrincipal CustomOAuth2User user) {
        boolean liked = likeService.isLiked(feedId, user.getUserId());
        Map<String, Object> response = new HashMap<>();
        response.put("liked", liked);
        return ResponseEntity.ok(response);
    }
}
