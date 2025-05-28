package com.motionmate.mapper.goods;

import com.motionmate.domain.goods.Goods;
import com.motionmate.dto.goods.product.GoodsRequestDto;
import com.motionmate.dto.goods.product.GoodsResponseDto;
import com.motionmate.service.redis.LimitedGoodsRedisService;
import com.motionmate.utils.JsonUtil;

import java.util.List;

public class GoodsMapper {

    public static GoodsResponseDto toDto(Goods goods, boolean liked, LimitedGoodsRedisService redisService) {


        String status;

        int stock = goods.getStock();
        if (goods.isLimited()) {
            stock = redisService.getStock(goods.getId()); // Redis 재고 반영
        }


        if (goods.isHidden()) {
            status = "HIDDEN";
        } else if (stock == 0) {
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
                .stock(stock)
                .liked(liked)
                .limited(goods.isLimited())
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

    public static List<GoodsResponseDto> toDtoList(List<Goods> goodsList, LimitedGoodsRedisService redisService){
        return goodsList.stream()
                .map(goods -> toDto(goods, false, redisService))
                .toList();
    }
}
