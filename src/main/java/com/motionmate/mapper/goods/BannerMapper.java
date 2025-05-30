package com.motionmate.mapper.goods;

import com.motionmate.domain.goods.Banner;
import com.motionmate.dto.goods.banner.BannerRequestDto;
import com.motionmate.dto.goods.banner.BannerResponseDto;

public class BannerMapper {

    // RequestDto → Entity
    public static Banner toEntity(BannerRequestDto dto) {
        return Banner.builder()
                .imageUrl(dto.getImageUrl())
                .bucketKey(dto.getBucketKey())
                .orderIndex(dto.getOrderIndex())
                .visible(dto.isVisible())
                .build();
    }

    // Entity → ResponseDto
    public static BannerResponseDto toResponseDto(Banner banner) {
        return BannerResponseDto.builder()
                .id(banner.getId())
                .imageUrl(banner.getImageUrl())
                .orderIndex(banner.getOrderIndex())
                .visible(banner.isVisible())
                .build();
    }
}
