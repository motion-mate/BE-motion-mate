package com.motionmate.dto.goods.banner;

import com.motionmate.dto.exercise.S3FileRequest;
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

    private S3FileRequest image;

    private Integer orderIndex;

    private boolean visible;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
