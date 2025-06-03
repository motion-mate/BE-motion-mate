package com.motionmate.controller.admin;

import com.motionmate.dto.feed.FeedResponseDto;
import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.service.feed.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/feeds")
public class AdminFeedController {

    private final FeedService feedService;

    // ✅ 모든 피드 전체 조회
    @GetMapping
    public ResponseEntity<List<FeedResponseDto>> getAllFeeds() {
        return ResponseEntity.ok(feedService.getAllFeeds());
    }

    // ✅ 관리자 피드 삭제
    @DeleteMapping("/{feedId}")
    public ResponseEntity<Void> deleteFeed(
            @PathVariable Long feedId,
            @AuthenticationPrincipal CustomOAuth2User adminUser) {
        feedService.delete(feedId, adminUser.getUser()); // Role.ADMIN 체크 포함돼 있어야 함
        return ResponseEntity.noContent().build();
    }
}
