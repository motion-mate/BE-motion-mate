package com.motionmate.dto.goods.event;

import com.motionmate.domain.goods.event.EventStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class GoodsEventResponseDto {
    private Long id;

    private String title;
    private String description;
    private String imageUrl;

    private String goodsName;
    private String goodsImageUrl;

    private int remainingStock;

    private LocalDate startDate;
    private LocalDate endDate;

    private EventStatus status;
}
