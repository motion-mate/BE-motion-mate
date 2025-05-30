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

    @GetMapping
    public ResponseEntity<List<NoticeResponseDto>> getAllNotices(){
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponseDto> getNotice(@PathVariable Long id){
        return ResponseEntity.ok(noticeService.getNotice(id));
    }

    @PostMapping
    // 추후 관리자 인증 필요
    public ResponseEntity<NoticeResponseDto> createNotice(@Valid @RequestBody NoticeRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(noticeService.createNotice(requestDto));
    }
}
