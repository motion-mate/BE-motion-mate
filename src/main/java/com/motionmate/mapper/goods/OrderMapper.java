package com.motionmate.mapper.goods;

import com.motionmate.domain.goods.Order;
import com.motionmate.dto.goods.order.OrderRequestDto;
import com.motionmate.dto.goods.order.OrderResponseDto;

public class OrderMapper {
    public static OrderResponseDto toDto(Order order) {
        int price = order.getGoods().getPrice();
        int quantity = order.getQuantity();
        return OrderResponseDto.builder()
                .orderId(order.getId())
                .goodsName(order.getGoods().getName())
                .price(price)
                .quantity(quantity)
                .totalPrice(price * quantity) // ✅ 총 가격 계산
                .orderedAt(order.getOrderedAt())
                .status(order.getStatus())
                .trackingNumber(order.getTrackingNumber())
                .courier(order.getCourier())
                .build();
    }

}
