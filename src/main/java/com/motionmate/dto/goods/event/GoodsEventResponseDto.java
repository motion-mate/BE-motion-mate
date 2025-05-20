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
    private LocalDate startDate;
    private LocalDate endDate;
    private EventStatus status;
    private int remainingStock;

}