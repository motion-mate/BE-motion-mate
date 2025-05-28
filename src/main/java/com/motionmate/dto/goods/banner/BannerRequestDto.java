package com.motionmate.dto.goods.banner;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BannerRequestDto {
    @NotBlank(message = "이미지 URL은 필수입니다")
    private String imageUrl;
    private String bucketKey;
    private Integer orderIndex;
    private boolean visible;
}
