package com.motionmate.dto.goods.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class OrderResponseDto {

    private Long orderId;
    private LocalDateTime orderedAt;
    private List<OrderItemDto> items;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class OrderItemDto {
        private Long goodsId;
        private String goodsName;
        private int quantity;
        private int unitPrice;
        private int totalPrice;
    }
}
