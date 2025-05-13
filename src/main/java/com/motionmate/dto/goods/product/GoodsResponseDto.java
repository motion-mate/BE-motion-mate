package com.motionmate.dto.goods.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GoodsResponseDto {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private int price;
    private int stock;
    private boolean liked;

    // ✅ 추가 필드
    private boolean isLimited;
    private String category;
    private String subCategory;
}
