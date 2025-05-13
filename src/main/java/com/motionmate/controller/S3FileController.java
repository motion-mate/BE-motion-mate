package com.motionmate.controller;

import com.motionmate.service.S3FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class S3FileController {
    private final S3FileService service;

    @PostMapping("/api/images/temp")
    public ResponseEntity<?> postMethodName(@RequestParam("tempFile")MultipartFile multipartFile){
        return service.uploadTempFile(multipartFile);
    }
}
