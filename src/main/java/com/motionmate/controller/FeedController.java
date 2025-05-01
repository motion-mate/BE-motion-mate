package com.motionmate.controller;

import com.motionmate.domain.user.User;
import com.motionmate.dto.feed.FeedDetailResponseDto;
import com.motionmate.dto.feed.FeedRequestDto;
import com.motionmate.dto.feed.FeedResponseDto;
import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService service;

    //피드 업로드
    @PostMapping
    public ResponseEntity<FeedResponseDto> upload(
            @RequestBody FeedRequestDto request,
            @AuthenticationPrincipal CustomOAuth2User user) {
        return ResponseEntity.ok(service.upload(request, user.getUserId()));
        }

    //전체 피드 조회
    @GetMapping
    public ResponseEntity<List<FeedResponseDto>> getAllFeed(@AuthenticationPrincipal CustomOAuth2User user){
        return ResponseEntity.ok(service.getAllFeed(user.getUserId()));
    }


    //피드 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<FeedDetailResponseDto> getFeedDetail(
            @PathVariable(name = "id") Long id,
            @AuthenticationPrincipal CustomOAuth2User user){
        return ResponseEntity.ok(service.getFeedDetail(id, user.getUserId()));
    }

    //피드 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<FeedDetailResponseDto> updateFeed(
            @PathVariable(name = "id") Long id,
            @RequestBody FeedRequestDto request,
            @AuthenticationPrincipal CustomOAuth2User user) {
        return ResponseEntity.ok(service.update(id, request, user.getUserId()));
    }

    //피드 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeed(
            @PathVariable(name = "id") Long id,
            @AuthenticationPrincipal CustomOAuth2User user) {
        service.delete(id, user.getUserId());
        return ResponseEntity.noContent().build();
    }


    // DELETE /{id}
    // POST /{id}/like
    // DELETE /{id}/like
}
