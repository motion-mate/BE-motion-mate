package com.motionmate.controller.goods;

import com.motionmate.service.goods.GoodsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goods")
public class GoodsS3UploadController {

    private final GoodsS3Service goodsS3Service;

    // 미리보기용 이미지 업로드 (temp path)
    @PostMapping("/image/temp")
    public ResponseEntity<?> uploadTempImage(@RequestParam("file") MultipartFile multipartFile) {
        return goodsS3Service.uploadTempFile(multipartFile);
    }
}
