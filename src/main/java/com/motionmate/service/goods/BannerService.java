package com.motionmate.service.goods;

import com.motionmate.domain.goods.Banner;
import com.motionmate.domain.goods.BannerRepository;
import com.motionmate.dto.exercise.S3FileRequest;
import com.motionmate.dto.exercise.S3FileResponse;
import com.motionmate.dto.goods.banner.BannerRequestDto;
import com.motionmate.dto.goods.banner.BannerResponseDto;
import com.motionmate.mapper.goods.BannerMapper;
import com.motionmate.mapper.s3.S3FileMapper;
import com.motionmate.utils.S3ServiceUtils;
import com.motionmate.service.s3.S3FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;
    private final S3FileService s3FileService;
    private final S3ServiceUtils s3ServiceUtils;

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
        S3FileRequest image = dto.getImage();
        int userPk = 103;

        if (image != null) {
            S3FileResponse moved = s3ServiceUtils.moveFromTempToUpload(image, userPk);
            image = S3FileMapper.toS3FileRequest(moved);
            s3ServiceUtils.deleteUserTempFiles(userPk);
        }

        Banner banner = BannerMapper.toEntity(dto, image);
        banner = bannerRepository.save(banner);
        return BannerMapper.toResponseDto(banner);
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

    public List<BannerResponseDto> getAllBannerList() {
        return bannerRepository.findAll(Sort.by(Sort.Direction.ASC, "orderIndex"))
                .stream()
                .map(BannerMapper::toResponseDto)
                .toList();
    }

    public BannerResponseDto getBannerById(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("배너를 찾을 수 없습니다."));
        return BannerMapper.toResponseDto(banner);
    }

    @Transactional
    public BannerResponseDto updateBanner(Long id, BannerRequestDto dto) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("배너를 찾을 수 없습니다."));

        S3FileRequest image = dto.getImage();
        int userPk = 103;

        if (image != null && !image.bucketKey().equals(banner.getBucketKey())) {
            // 기존 이미지 삭제
            s3FileService.deleteFile(banner.getBucketKey());

            // 새 이미지 처리
            S3FileResponse moved = s3ServiceUtils.moveFromTempToUpload(image, userPk);
            image = S3FileMapper.toS3FileRequest(moved);
            banner.updateImage(image); // 아래에 정의 필요
            s3ServiceUtils.deleteUserTempFiles(userPk);
        }

        banner.updateInfo(dto); // 아래에 정의 필요
        return BannerMapper.toResponseDto(banner);
    }
}
