package com.motionmate.controller.goods;

import com.motionmate.dto.exercise.S3FileResponse;
import com.motionmate.dto.exercise.S3FileRequest;
import com.motionmate.dto.goods.banner.BannerRequestDto;
import com.motionmate.dto.goods.banner.BannerResponseDto;
import com.motionmate.service.goods.BannerService;
import com.motionmate.service.s3.S3FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BannerController {

    private final BannerService bannerService;
    private final S3FileService s3FileService;

    // ✅ 공개 배너 조회
    @GetMapping("/banners")
    public List<String> getBanners() {
        return bannerService.getVisibleBannerUrls();
    }

    // ✅ 배너 등록 (S3에 업로드 + DB 저장)
    @PostMapping("/banners")
    public BannerResponseDto registerBanner(@RequestBody @Valid BannerRequestDto requestDto) {
        return bannerService.saveBanner(requestDto);
    }


    // ✅ 배너 삭제
    @DeleteMapping("/banners/{id}")
    public void deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
    }
}
