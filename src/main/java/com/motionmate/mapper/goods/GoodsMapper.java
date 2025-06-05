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
                .bucketKey(goods.getBucketKey())  // ✅ 추가
                .orgName(goods.getOrgName())      // ✅ 추가
                .price(goods.getPrice())
                .stock(stock)
                .liked(liked)
                .limited(goods.isLimited())
                .category(goods.getCategory())
                .subCategory(goods.getSubCategory())
                .colors(JsonUtil.fromJsonArray(goods.getColorsJson()))
                .status(status)
                .hidden(goods.isHidden()) // ✅ 이 줄 추가
                .sizes(JsonUtil.fromJsonArray(goods.getSizesJson()))
                .build();
    }

    // 결제 시 email 이 필수라서 toDto 오버로딩
    public static GoodsResponseDto toDto(Goods goods, boolean liked, LimitedGoodsRedisService redisService, String email) {

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
                .email(email)  // 새 필드 추가
                .build();
    }


    public static Goods toEntity(GoodsRequestDto dto, com.motionmate.dto.exercise.S3FileRequest imageInfo) {
        return new Goods(
                dto.getName(),
                dto.getDescription(),
                imageInfo.url(),
                imageInfo.bucketKey(),
                imageInfo.orgName(),
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
