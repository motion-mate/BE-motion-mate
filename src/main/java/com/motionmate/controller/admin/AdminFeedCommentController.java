package com.motionmate.controller.admin;

import com.motionmate.dto.feed.FeedCommentResponseDto;
import com.motionmate.service.feed.FeedCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/feeds/comments")
public class AdminFeedCommentController {

    private final FeedCommentService feedCommentService;

    // ✅ 특정 피드에 달린 모든 댓글 조회
    @GetMapping("/{feedId}")
    public ResponseEntity<List<FeedCommentResponseDto>> getAllComments(@PathVariable Long feedId) {
        return ResponseEntity.ok(feedCommentService.getAllComments(feedId, null));
    }

    // ✅ 댓글 삭제 (관리자 권한)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        feedCommentService.deleteCommentByAdmin(commentId); // 서비스 따로 만들어줘야 함
        return ResponseEntity.noContent().build();
    }
}
