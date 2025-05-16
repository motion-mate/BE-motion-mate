package com.motionmate.mapper.goods;

import com.motionmate.domain.goods.Goods;
import com.motionmate.dto.goods.product.GoodsRequestDto;
import com.motionmate.dto.goods.product.GoodsResponseDto;
import com.motionmate.utils.JsonUtil;

public class GoodsMapper {

    public static GoodsResponseDto toDto(Goods goods, boolean liked) {
        return GoodsResponseDto.builder()
                .id(goods.getId())
                .name(goods.getName())
                .description(goods.getDescription())
                .imageUrl(goods.getImageUrl())
                .price(goods.getPrice())
                .stock(goods.getStock() != null ? goods.getStock() : 0)
                .liked(liked)
                .isLimited(goods.isLimited())
                .category(goods.getCategory())
                .subCategory(goods.getSubCategory())
                .colors(JsonUtil.fromJsonArray(goods.getColorsJson()))
                .sizes(JsonUtil.fromJsonArray(goods.getSizesJson()))
                .build();
    }

    public static Goods toEntity(GoodsRequestDto dto) {
        return new Goods(
                dto.getName(),
                dto.getDescription(),
                dto.getImageUrl(),
                dto.getStock(),
                dto.getPrice(),
                dto.getIsLimited(),
                dto.getCategory(),
                dto.getSubCategory(),
                JsonUtil.toJsonArray(dto.getColors()),
                JsonUtil.toJsonArray(dto.getSizes())
        );
    }
}
