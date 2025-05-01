package com.motionmate.dto.goods.order;

import com.motionmate.domain.goods.Order;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderResponseDto {
    private Long orderId;
    private String goodsName;
    private Integer quantity;
    private Integer price;  // ✅ price 필드
    private LocalDateTime orderedAt;

    public static OrderResponseDto fromEntity(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getId())
                .goodsName(order.getGoods().getName())
                .quantity(order.getQuantity())
                .price(order.getGoods().getPrice()) // ✅ 여기 추가!
                .orderedAt(order.getOrderedAt())
                .build();
    }
}
