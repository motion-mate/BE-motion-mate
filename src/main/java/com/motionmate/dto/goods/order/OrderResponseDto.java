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
    private Integer price;
    private Integer totalPrice; // 총가격
    private LocalDateTime orderedAt;
    private Order.OrderStatus status;
    private String trackingNumber;
    private String courier;
}