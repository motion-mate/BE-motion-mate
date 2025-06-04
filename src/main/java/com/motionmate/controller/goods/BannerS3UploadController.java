package com.motionmate.controller.goods;

import com.motionmate.service.goods.BannerS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/banner")
public class BannerS3UploadController {

    private final BannerS3Service bannerS3Service;

    @PostMapping("/image/temp")
    public ResponseEntity<?> uploadTempImage(@RequestParam("file") MultipartFile multipartFile) {
        return bannerS3Service.uploadTempFile(multipartFile);
    }
}
