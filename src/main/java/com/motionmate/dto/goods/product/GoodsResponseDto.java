package com.motionmate.dto.goods.product;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String bucketKey;
    private String orgName;
    private int price;
    private int stock;
    private boolean soldOut; // ✅ 이 필드 추가 (Redis 기준)
    private boolean liked;

    private boolean limited;
    private String category;
    private String subCategory;
    private String status; // "FOR_SALE", "SOLD_OUT", "HIDDEN"


    private List<String> colors;
    private List<String> sizes;
    private boolean hidden; // ✅ 이 줄 추가


    private String email;
}
