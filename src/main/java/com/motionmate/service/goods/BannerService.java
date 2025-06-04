package com.motionmate.service.goods;

import com.motionmate.domain.goods.Banner;
import com.motionmate.domain.goods.BannerRepository;
import com.motionmate.dto.goods.banner.BannerRequestDto;
import com.motionmate.dto.goods.banner.BannerResponseDto;
import com.motionmate.mapper.goods.BannerMapper;
import com.motionmate.service.s3.S3FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;
    private final S3FileService s3FileService;

    /**
     * 현재 시점에 노출 가능한 배너들의 이미지 URL 목록을 반환
     * - visible = true
     * - 시작일은 현재보다 이전
     * - 종료일은 현재보다 이후
     */
    public List<String> getVisibleBannerUrls() {
        LocalDateTime now = LocalDateTime.now();
        return bannerRepository
                .findAllByVisibleIsTrueAndStartDateBeforeAndEndDateAfterOrderByOrderIndexAsc(now, now)
                .stream()
                .map(Banner::getImageUrl)
                .toList();
    }

    /**
     * 배너 정보를 저장하고 응답 DTO를 반환
     * - BannerRequestDto → Entity 변환
     * - DB에 저장 후 응답용 DTO로 변환
     */
    public BannerResponseDto saveBanner(BannerRequestDto dto) {
        Banner banner = BannerMapper.toEntity(dto);              // DTO → Entity
        banner = bannerRepository.save(banner);                  // DB 저장
        return BannerMapper.toResponseDto(banner);               // Entity → Response DTO
    }

    /**
     * 배너를 삭제
     * - S3에 저장된 이미지도 함께 삭제
     * - DB에서 배너 정보 제거
     */
    public void deleteBanner(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("배너를 찾을 수 없습니다."));

        s3FileService.deleteFile(banner.getBucketKey());         // S3에서 파일 삭제
        bannerRepository.delete(banner);                         // DB에서 삭제
    }
}
