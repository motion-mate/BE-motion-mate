package com.motionmate.controller.notice;

import com.motionmate.dto.Notice.NoticeRequestDto;
import com.motionmate.dto.Notice.NoticeResponseDto;
import com.motionmate.service.noticesevice.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // 📌 전체 공지 목록 조회
    @GetMapping
    public ResponseEntity<List<NoticeResponseDto>> getAllNotices() {
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    // 📌 공지 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponseDto> getNotice(@PathVariable Long id) {
        return ResponseEntity.ok(noticeService.getNotice(id));
    }

    // 📌 공지 등록 (추후 관리자 권한 필요)
    @PostMapping
    public ResponseEntity<NoticeResponseDto> createNotice(@Valid @RequestBody NoticeRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(noticeService.createNotice(requestDto));
    }
}

