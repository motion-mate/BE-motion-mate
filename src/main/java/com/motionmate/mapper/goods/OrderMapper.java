package com.motionmate.mapper.goods;

import com.motionmate.domain.goods.Goods;
import com.motionmate.domain.goods.Order;
import com.motionmate.domain.goods.OrderItem;
import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.order.OrderItemRequestDto;
import com.motionmate.dto.goods.order.OrderResponseDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    // ✅ 주문 엔티티 생성
    public Order toOrderEntity(User user) {
        return Order.builder()
                .user(user)
                .orderedAt(LocalDateTime.now())
<<<<<<< HEAD
                .status(Order.OrderStatus.READY) // enum 타입 사용
=======
                .status(Order.OrderStatus.READY) // enum 사용
>>>>>>> 37bf0df6c36ca39daeaa6c15258b39fec675af8e
                .orderNumber(UUID.randomUUID().toString())
                .build();
    }

    // ✅ OrderItem 리스트 생성
    public List<OrderItem> toOrderItemEntityList(List<OrderItemRequestDto> requestItems, List<Goods> goodsList) {
        return requestItems.stream().map(req -> {
            Goods matchedGoods = goodsList.stream()
                    .filter(g -> g.getId().equals(req.getGoodsId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("상품 ID [" + req.getGoodsId() + "] 를 찾을 수 없습니다."));

            return OrderItem.builder()
                    .goods(matchedGoods)
                    .quantity(req.getQuantity())
                    .unitPrice(matchedGoods.getPrice())
                    .build();
        }).collect(Collectors.toList());
    }

    // ✅ 주문 응답 DTO 변환
    public OrderResponseDto toResponseDto(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getId())
                .orderedAt(order.getOrderedAt())
                .status(order.getStatus())
                .trackingNumber(order.getTrackingNumber())
                .courier(order.getCourier())
                .items(order.getOrderItems().stream()
                        .map(item -> OrderResponseDto.OrderItemDto.builder()
                                .goodsId(item.getGoods().getId())
                                .goodsName(item.getGoods().getName())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .totalPrice(item.getTotalPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
