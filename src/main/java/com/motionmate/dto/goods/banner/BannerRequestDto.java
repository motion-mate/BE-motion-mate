package com.motionmate.dto.goods.banner;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BannerRequestDto {

    @NotBlank(message = "배너 제목은 필수입니다.")
    private String title;

    private String link;

    @NotBlank(message = "이미지 URL은 필수입니다.")
    private String imageUrl;

    private String bucketKey;

    private Integer orderIndex;

    private boolean visible;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
