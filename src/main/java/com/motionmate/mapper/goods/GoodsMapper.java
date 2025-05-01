package com.motionmate.mapper.goods;

import com.motionmate.domain.goods.Goods;
import com.motionmate.dto.goods.product.GoodsRequestDto;
import com.motionmate.dto.goods.product.GoodsResponseDto;

public class GoodsMapper {
    public static GoodsResponseDto toDto(Goods goods, boolean liked) {
        return GoodsResponseDto.builder()
                .id(goods.getId())
                .name(goods.getName())
                .description(goods.getDescription())
                .imageUrl(goods.getImageUrl())
                .price(goods.getPrice())
                .stock(goods.getStock())
                .liked(liked)
                .build();
    }

    public static Goods toEntity(GoodsRequestDto dto) {
        return new Goods(
                dto.getName(),
                dto.getDescription(),
                dto.getImageUrl(),
                dto.getStock(),
                dto.getPrice()
        );
    }
}
