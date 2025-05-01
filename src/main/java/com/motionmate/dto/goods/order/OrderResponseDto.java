package com.motionmate.dto.goods.order;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderResponseDto {
    private Long orderId;
    private String goodsName;
    private Integer quantity;
    private Integer price;
    private LocalDateTime orderedAt;
}
