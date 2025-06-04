package com.motionmate.mapper.goods;

import com.motionmate.domain.goods.Banner;
import com.motionmate.dto.exercise.S3FileRequest;
import com.motionmate.dto.goods.banner.BannerRequestDto;
import com.motionmate.dto.goods.banner.BannerResponseDto;

import java.time.LocalDateTime;

public class BannerMapper {

    // RequestDto → Entity
    public static Banner toEntity(BannerRequestDto dto, S3FileRequest image) {
        return Banner.builder()
                .title(dto.getTitle())
                .link(dto.getLink())
                .imageUrl(image.url())
                .bucketKey(image.bucketKey())
                .orderIndex(dto.getOrderIndex())
                .visible(dto.isVisible())
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(30))
                .build();
    }


    // Entity → ResponseDto
    public static BannerResponseDto toResponseDto(Banner banner) {
        return BannerResponseDto.builder()
                .id(banner.getId())
                .title(banner.getTitle())                  // ✅ title 추가
                .link(banner.getLink())                    // ✅ link 추가
                .imageUrl(banner.getImageUrl())
                .orderIndex(banner.getOrderIndex())
                .visible(banner.isVisible())
                .build();
    }
}
