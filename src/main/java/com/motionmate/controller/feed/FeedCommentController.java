package com.motionmate.controller.feed;

import com.motionmate.dto.feed.FeedCommentRequestDto;
import com.motionmate.dto.feed.FeedCommentResponseDto;
import com.motionmate.dto.feed.FeedCommentUpdateDto;
import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.service.feed.FeedCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds")
public class FeedCommentController {

    private final FeedCommentService feedCommentService;

    //댓글 등록
    @PostMapping("/comments/{feedId}")
    public ResponseEntity<FeedCommentResponseDto> createComment(
            @PathVariable Long feedId,
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestBody FeedCommentRequestDto dto) {
        FeedCommentResponseDto response = feedCommentService.createComment(feedId, user.getUserId(), dto);
        return ResponseEntity.ok(response);
    }

    //댓글 전체 조회
    @GetMapping("/comments/{feedId}")
    public  ResponseEntity<List<FeedCommentResponseDto>> getAllComments(
            @PathVariable Long feedId,
            @AuthenticationPrincipal CustomOAuth2User user) {
        Long userId = (user != null) ? user.getUserId() : null;
        return ResponseEntity.ok(feedCommentService.getAllComments(feedId, userId));
    }

    //댓글 미리 보기
    @GetMapping("/comments/{feedId}/preview")
    public ResponseEntity<List<FeedCommentResponseDto>> getPreviewComments(
            @PathVariable Long feedId) {
        return ResponseEntity.ok(feedCommentService.getPreviewComments(feedId));
    }

    //댓글 수정
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<FeedCommentResponseDto> updateComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestBody FeedCommentUpdateDto dto) {
        FeedCommentResponseDto response = feedCommentService.updateComment(commentId, user.getUserId(), dto);
        return ResponseEntity.ok(response);
    }

    //댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomOAuth2User user) {
        feedCommentService.deleteComment(commentId, user.getUserId());
        return ResponseEntity.noContent().build();
    }

}
