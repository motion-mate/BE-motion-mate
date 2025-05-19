package com.motionmate.dto.goods.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

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

    private boolean isLimited;
    private String category;
    private String subCategory;

    private List<String> colors;
    private List<String> sizes;
}
