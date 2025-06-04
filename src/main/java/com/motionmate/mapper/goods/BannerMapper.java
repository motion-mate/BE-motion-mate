package com.motionmate.mapper.goods;

import com.motionmate.domain.goods.Banner;
import com.motionmate.dto.goods.banner.BannerRequestDto;
import com.motionmate.dto.goods.banner.BannerResponseDto;

import java.time.LocalDateTime;

public class BannerMapper {

    // RequestDto → Entity
    public static Banner toEntity(BannerRequestDto dto) {
        return Banner.builder()
                .title(dto.getTitle())                      // ✅ title 추가
                .link(dto.getLink())                        // ✅ link 추가
                .imageUrl(dto.getImageUrl())
                .bucketKey(dto.getBucketKey())
                .orderIndex(dto.getOrderIndex())
                .visible(dto.isVisible())
                .startDate(LocalDateTime.now())             // ✅ startDate
                .endDate(LocalDateTime.now().plusDays(30))  // ✅ endDate
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
