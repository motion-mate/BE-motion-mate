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

    public List<String> getVisibleBannerUrls(){
        LocalDateTime now = LocalDateTime.now();
        return bannerRepository
                .findAllByVisibleIsTrueAndStartDateBeforeAndEndDateAfterOrderByOrderIndexAsc(now, now)
                .stream()
                .map(Banner::getImageUrl)
                .toList();
    }

    public BannerResponseDto saveBanner(BannerRequestDto dto){
        Banner banner = BannerMapper.toEntity(dto);
        banner = bannerRepository.save(banner);
        return BannerMapper.toResponseDto(banner);
    }

    public void deleteBanner(Long id){
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("배너를 찾을 수 없습니다."));

        // s3에서 이미지 삭제
        s3FileService.deleteFile(banner.getBucketKey());
        //DB에서 삭제
        bannerRepository.delete(banner);
    }
}

