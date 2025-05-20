package com.motionmate.dto.goods.order;

import com.motionmate.domain.goods.Order;
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
    private Order.OrderStatus status;
    private String trackingNumber;
    private String courier;
    private List<OrderItemDto> items;

    // ✅ 유틸 메서드: 총 금액 계산
    public int getTotalAmount() {
        return items.stream()
                .mapToInt(OrderItemDto::getTotalPrice)
                .sum();
    }

    // ✅ 유틷 메서드: 첫 상품명
    public String getFirstProductName() {
        return (items != null && !items.isEmpty())
                ? items.get(0).getGoodsName()
                : "";
    }

    // ✅ 내부 상품 DTO
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
