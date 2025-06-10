package com.motionmate.dto.goods.product;

import com.motionmate.dto.exercise.S3FileRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsRequestDto {

    @NotBlank(message = "상품 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "상품 설명은 필수입니다.")
    private String description;

    @NotNull(message = "이미지는 필수입니다.")
    private S3FileRequest image;

    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer price;

    @NotNull(message = "재고는 필수입니다.")
    @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
    private Integer stock;

    @NotNull(message = "한정 여부는 필수입니다.")
    private Boolean limited;

    @NotBlank(message = "카테고리는 필수입니다.")
    private String category;

    @NotBlank(message = "하위 카테고리는 필수입니다.")
    private String subCategory;

    private List<String> colors;
    private List<String> sizes;
}

