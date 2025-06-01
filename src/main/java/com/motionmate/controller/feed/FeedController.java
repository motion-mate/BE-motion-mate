package com.motionmate.controller.feed;

import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.dto.feed.FeedDetailResponseDto;
import com.motionmate.dto.feed.FeedRequestDto;
import com.motionmate.dto.feed.FeedResponseDto;
import com.motionmate.global.exception.CustomException;
import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.service.feed.FeedService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService service;
    private final UserRepository userRepository;

    //피드 업로드
    @PostMapping("/upload")
    public ResponseEntity<FeedResponseDto> upload(
            @RequestBody FeedRequestDto request,
            @AuthenticationPrincipal CustomOAuth2User user) {

        FeedResponseDto response = service.upload(request, user.getUser());
        return ResponseEntity.ok(response);
    }

    //전체 피드 조회
    @GetMapping
    public ResponseEntity<List<FeedResponseDto>> getFeeds(
            @RequestParam(required = false) Long lastFeedId,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomOAuth2User user) {

        if (user == null) {
            return ResponseEntity.ok(service.getFeedsByCursor(lastFeedId, size, null));
        }

        return ResponseEntity.ok(service.getFeedsByCursor(lastFeedId, size, user.getUser()));
    }


    //피드 상세 조회
    @GetMapping("/{feedId}")
    public ResponseEntity<FeedDetailResponseDto> getFeedDetail(
            @PathVariable Long feedId,
            @AuthenticationPrincipal @Nullable CustomOAuth2User user) {
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
        service.delete(feedId, user.getUser());
        return ResponseEntity.noContent().build();
    }

    //본인 피드 조회
    @GetMapping("/my")
    public ResponseEntity<List<FeedResponseDto>> getMyFeeds(
            @AuthenticationPrincipal CustomOAuth2User user) {

        List<FeedResponseDto> myFeeds = service.getMyFeeds(user.getUser());

        return ResponseEntity.ok(myFeeds);
    }


}
