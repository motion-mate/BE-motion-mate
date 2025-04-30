package com.motionmate.dto.goods.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter // 모든 필드에 대한 getter 생성
@NoArgsConstructor // 기본 생성자 (JSON → 객체 변환용)
public class GoodsRequestDto {

    @NotBlank(message = "상품 이름은 필수입니다.") // null, 빈 문자열, 공백 불가
    private String name;

    @NotBlank(message = "상품 설명은 필수입니다.")
    private String description;

    @NotBlank(message = "이미지 URL은 필수입니다.")
    private String imageUrl;

    @NotNull(message = "가격은 필수입니다.") // null 불가
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.") // 최소값 제한
    private Integer price;

    @NotNull(message = "재고는 필수입니다.")
    @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
    private Integer stock;
}
