package com.motionmate.dto.goods.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private LocalDateTime orderedAt;
    private String status;
    private List<OrderItemDto> items;

    public int getTotalAmount() {
        return items.stream().mapToInt(OrderItemDto::getTotalPrice).sum();
    }

    public String getFirstProductName() {
        return items != null && !items.isEmpty() ? items.get(0).getGoodsName() : "";
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class OrderItemDto {
        private Long goodsId;
        private String goodsName;
        private int quantity;
        private int unitPrice;
        private int totalPrice;
    }
}