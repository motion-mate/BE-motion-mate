package com.motionmate.mapper.goods;

import com.motionmate.domain.goods.Order;
import com.motionmate.dto.goods.order.OrderResponseDto;

public class OrderMapper {
    public static OrderResponseDto toDto(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getId())
                .goodsName(order.getGoods().getName())
                .price(order.getGoods().getPrice())
                .quantity(order.getQuantity())
                .orderedAt(order.getOrderedAt())
                .build();
    }
}
