package com.motionmate.dto.goods.banner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BannerResponseDto {

    private Long id;
    private String imageUrl;
    private Integer orderIndex;
    private boolean visible;
}
