package com.motionmate.mapper.goods;

import com.motionmate.domain.goods.Goods;
import com.motionmate.dto.goods.product.GoodsRequestDto;
import com.motionmate.dto.goods.product.GoodsResponseDto;
import com.motionmate.utils.JsonUtil;

import java.util.List;

public class GoodsMapper {

    public static GoodsResponseDto toDto(Goods goods, boolean liked) {

        String status;
        if (goods.isHidden()) {
            status = "HIDDEN";
        } else if (goods.getStock() != null && goods.getStock() == 0) {
            status = "SOLD_OUT";
        } else {
            status = "FOR_SALE";
        }


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
                .status(status)
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
                dto.getLimited(),
                dto.getCategory(),
                dto.getSubCategory(),
                JsonUtil.toJsonArray(dto.getColors()),
                JsonUtil.toJsonArray(dto.getSizes())
        );
    }

    public static List<GoodsResponseDto> toDtoList(List<Goods> goodsList){
        return goodsList.stream()
                .map(goods -> toDto(goods, false))
                .toList();
    }
}
