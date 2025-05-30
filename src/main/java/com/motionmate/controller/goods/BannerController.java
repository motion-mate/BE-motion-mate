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
public class BannerController {

    private final BannerService bannerService;
    private final S3FileService s3FileService;

    // ✅ 공개 배너 조회
    @GetMapping("/banners")
    public List<String> getBanners() {
        return bannerService.getVisibleBannerUrls();
    }

    // ✅ 배너 등록 (S3에 업로드 + DB 저장)
    @PostMapping(value = "/admin/banners", consumes = "multipart/form-data")
    public BannerResponseDto uploadBanner(
            @RequestPart("file") MultipartFile file,
            @RequestPart("dto") @Valid BannerRequestDto requestDto
    ) throws IOException {

        // 1. S3에 임시 업로드 → 이미지 URL 획득
        S3FileResponse response = (S3FileResponse) s3FileService.uploadTempFile(file).getBody();
        String imageUrl = response.url();

        // 2. DTO 불변성 유지: 새 객체 생성
        BannerRequestDto newDto = new BannerRequestDto(
                imageUrl,
                response.bucketKey(),
                requestDto.getOrderIndex(),
                requestDto.isVisible()
        );

        // 3. 저장 및 응답
        return bannerService.saveBanner(newDto);
    }

    // ✅ 배너 삭제
    @DeleteMapping("/admin/banners/{id}")
    public void deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
    }
}
