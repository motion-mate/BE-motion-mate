package com.motionmate.mapper.goods;

import com.motionmate.domain.goods.event.GoodsEvent;
import com.motionmate.dto.goods.event.GoodsEventRequestDto;
import com.motionmate.dto.goods.event.GoodsEventResponseDto;

public class GoodsEventMapper {

    public static GoodsEvent toEntity(GoodsEventRequestDto dto) {
        return GoodsEvent.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .imageUrl(dto.getImageUrl())
                .goodsName(dto.getGoodsName())
                .goodsImageUrl(dto.getGoodsImageUrl())
                .eventStock(dto.getEventStock())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(com.motionmate.domain.goods.event.EventStatus.ONGOING)
                .build();
    }

    public static GoodsEventResponseDto toDto(GoodsEvent event, int remainingStock) {
        return GoodsEventResponseDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .imageUrl(event.getImageUrl())
                .goodsName(event.getGoodsName())
                .goodsImageUrl(event.getGoodsImageUrl())
                .remainingStock(remainingStock)
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .status(event.getStatus())
                .build();
    }
}
