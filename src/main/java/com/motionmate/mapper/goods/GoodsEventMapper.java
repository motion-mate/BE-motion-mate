package com.motionmate.mapper.goods;

import com.motionmate.domain.goods.event.GoodsEvent;
import com.motionmate.dto.goods.event.GoodsEventRequestDto;
import com.motionmate.dto.goods.event.GoodsEventResponseDto;

public class GoodsEventMapper {

    public static GoodsEventResponseDto toDto(GoodsEvent event, int remainingStock) {
        return GoodsEventResponseDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .imageUrl(event.getImageUrl())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .status(event.getStatus())
                .remainingStock(remainingStock)
                .build();
    }

    public static GoodsEvent toEntity(GoodsEventRequestDto dto) {
        return GoodsEvent.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .imageUrl(dto.getImageUrl())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(dto.getStatus())
                .stock(dto.getStock())
                .build();
    }
}
