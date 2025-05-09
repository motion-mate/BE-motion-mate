package com.motionmate.controller;

import com.motionmate.domain.user.User;
import com.motionmate.dto.feed.FeedDetailResponseDto;
import com.motionmate.dto.feed.FeedRequestDto;
import com.motionmate.dto.feed.FeedResponseDto;
import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.service.FeedLikeService;
import com.motionmate.service.FeedService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService service;

    //피드 업로드
    @PostMapping("/upload")
    public ResponseEntity<FeedResponseDto> upload(
            @RequestBody FeedRequestDto request,
            @AuthenticationPrincipal CustomOAuth2User user) {
        FeedResponseDto response = service.upload(request, user.getUserId());
        return ResponseEntity.ok(response);
        }

    //전체 피드 조회
    @GetMapping
    public ResponseEntity<List<FeedResponseDto>> getFeeds(
            @RequestParam(required = false) Long lastFeedId,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomOAuth2User user) {
        Long userId = (user != null) ? user.getUserId() : null;
        return ResponseEntity.ok(service.getFeedsByCursor(lastFeedId, size, userId));
    }


    //피드 상세 조회
    @GetMapping("/{feedId}")
    public ResponseEntity<FeedDetailResponseDto> getFeedDetail(
            @PathVariable Long feedId,
            @AuthenticationPrincipal @Nullable CustomOAuth2User user){
        Long userId = (user != null) ? user.getUserId() : null;
        return ResponseEntity.ok(service.getFeedDetail(feedId, userId));
    }

    //피드 게시글 수정

    @PutMapping("/{feedId}")
    public ResponseEntity<FeedDetailResponseDto> updateFeed(
            @PathVariable Long feedId,
            @RequestBody FeedRequestDto request,
            @AuthenticationPrincipal CustomOAuth2User user) {
        return ResponseEntity.ok(service.update(feedId, request, user.getUserId()));
    }

    //피드 삭제
    @DeleteMapping("/{feedId}")
    public ResponseEntity<Void> deleteFeed(
            @PathVariable Long feedId,
            @AuthenticationPrincipal CustomOAuth2User user) {
        service.delete(feedId, user.getUserId());
        return ResponseEntity.noContent().build();
    }

    //좋아요 누른 피드 조회
    @GetMapping("/liked")
    public ResponseEntity<List<FeedResponseDto>> getFeedsLikedByUser(
            @AuthenticationPrincipal CustomOAuth2User user) {
       List<FeedResponseDto> likedFeeds = service.getFeedsLikedByUser(user.getUserId());
       return ResponseEntity.ok(likedFeeds);
    }


}
