package com.motionmate.mapper.goods;

import com.motionmate.domain.delivery.Delivery;
import com.motionmate.domain.goods.Goods;
import com.motionmate.domain.goods.Order;
import com.motionmate.domain.goods.OrderItem;
import com.motionmate.domain.goods.ReviewRepository;
import com.motionmate.domain.user.User;
import com.motionmate.dto.delivery.DeliveryResponseDto;
import com.motionmate.dto.goods.order.OrderItemRequestDto;
import com.motionmate.dto.goods.order.OrderItemResponseDto;
import com.motionmate.dto.goods.order.OrderResponseDto;

import com.motionmate.mapper.delivery.DeliveryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ReviewRepository reviewRepository;

    public Order toOrderEntity(User user) {
        return Order.builder()
                .user(user)
                .orderedAt(LocalDateTime.now())
                .status(Order.OrderStatus.READY)
                .orderNumber(UUID.randomUUID().toString())
                .build();
    }

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

    public OrderResponseDto toResponseDto(Order order) {
        User user = order.getUser();
        Delivery delivery = order.getDelivery();

            DeliveryResponseDto deliveryDto = delivery != null ? DeliveryResponseDto.builder()
                .recipientName(delivery.getRecipientName())
                .phoneNumber(delivery.getPhoneNumber())
                .address(delivery.getAddress())
                .zipcode(delivery.getZipcode())
                    .courier(delivery.getCourier())               // ✅ 추가
                    .trackingNumber(delivery.getTrackingNumber()) // ✅ 추가
                .build() : null;

        return OrderResponseDto.builder()
                .orderId(order.getId())
                .orderedAt(order.getOrderedAt())
                .status(order.getStatus())
                .items(order.getOrderItems().stream()
                        .map(item -> OrderItemResponseDto.builder()
                                .goodsId(item.getGoods().getId())
                                .goodsName(item.getGoods().getName())
                                .goodsImageUrl(item.getGoods().getImageUrl())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .totalPrice(item.getTotalPrice())
                                .hasReview(reviewRepository.existsByUserAndGoods(user, item.getGoods()))
                                .build())
                        .collect(Collectors.toList()))
                .delivery(deliveryDto)
                .build();
    }
}