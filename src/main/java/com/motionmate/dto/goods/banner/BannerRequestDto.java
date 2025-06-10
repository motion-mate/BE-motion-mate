package com.motionmate.dto.goods.banner;

import com.motionmate.dto.exercise.S3FileRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BannerRequestDto {

    @NotBlank(message = "배너 제목은 필수입니다.")
    private String title;

    private String link;

    @NotNull(message = "이미지 정보는 필수입니다.")
    private S3FileRequest image;

    private Integer orderIndex;
    private boolean visible;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}

