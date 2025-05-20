package com.motionmate.controller;

import com.motionmate.service.FeedS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/feed")
public class FeedS3UploadController {

    private final FeedS3Service feedS3Service;

    //S3 이미지 업로드
    @PostMapping("/image-upload")
    public ResponseEntity<?> uploadFeedImage(@RequestParam MultipartFile file) {
        return feedS3Service.uploadTempFile(file);
    }
}
