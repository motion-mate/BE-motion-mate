package com.motionmate.controller.admin;

import com.motionmate.dto.goods.banner.BannerRequestDto;
import com.motionmate.dto.goods.banner.BannerResponseDto;
import com.motionmate.service.goods.BannerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/banners")
public class AdminBannerController {

    private final BannerService bannerService;

    // ✅ 전체 배너 조회 (관리자용)
    @GetMapping
    public List<BannerResponseDto> getAllBanners() {
        return bannerService.getAllBannerList();
    }

    // ✅ 배너 단건 조회
    @GetMapping("/{id}")
    public BannerResponseDto getBannerById(@PathVariable Long id) {
        return bannerService.getBannerById(id);
    }

    // ✅ 배너 등록
    @PostMapping
    public BannerResponseDto registerBanner(@RequestBody @Valid BannerRequestDto requestDto) {
        return bannerService.saveBanner(requestDto);
    }

    // ✅ 배너 수정
    @PutMapping("/{id}")
    public BannerResponseDto updateBanner(@PathVariable Long id,
                                          @RequestBody @Valid BannerRequestDto requestDto) {
        return bannerService.updateBanner(id, requestDto);
    }

    // ✅ 배너 삭제
    @DeleteMapping("/{id}")
    public void deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
    }
}