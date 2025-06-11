package com.motionmate.dto.goods.banner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class BannerResponseDto {

    private Long id;

    private String title;

    private String link;

    private String imageUrl;

    private String bucketKey;

    private String orgName;

    private Integer orderIndex;

    private boolean visible;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
