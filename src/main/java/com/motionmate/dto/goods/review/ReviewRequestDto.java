package com.motionmate.dto.goods.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewRequestDto {
    @NotNull (message = "상품 ID는 필수입니다.")
    private Long goodsId;

    @Min(value = 1, message = "평점은 1 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 5이하이어야 합니다.")
    private int rating;

    @NotBlank(message = "리뷰 내용을 입력해수제요")
    private String content;
}
