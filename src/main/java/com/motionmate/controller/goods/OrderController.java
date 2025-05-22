package com.motionmate.controller.goods;

import com.motionmate.domain.goods.*;
import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.order.OrderRequestDto;
import com.motionmate.dto.goods.order.OrderResponseDto;
import com.motionmate.dto.goods.order.OrderStatusUpdateDto;
import com.motionmate.dto.goods.order.TrackingUpdateDto;
import com.motionmate.dto.goods.product.GoodsRequestDto;
import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.service.goods.GoodsService;
import com.motionmate.service.goods.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.motionmate.mapper.goods.OrderMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final GoodsRepository goodsRepository;
    private final OrderMapper orderMapper;
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @Valid @RequestBody OrderRequestDto requestDto,
            @AuthenticationPrincipal CustomOAuth2User userPrincipal) {

        User user = userPrincipal.getUser();

        List<Goods> goodsList = goodsRepository.findAllById(
                requestDto.getItems().stream()
                        .map(item -> item.getGoodsId())
                        .toList()
        );

        Order order = orderMapper.toOrderEntity(user);
        List<OrderItem> orderItems = orderMapper.toOrderItemEntityList(requestDto.getItems(), goodsList);
        order.applyOrderItems(orderItems);

        orderRepository.save(order);

        return ResponseEntity.ok(orderMapper.toResponseDto(order));
    }

    @GetMapping
    public List<OrderResponseDto> getOrders(@AuthenticationPrincipal CustomOAuth2User userPrincipal) {
        User user = userPrincipal.getUser();
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }





    // ✅ 주문 상태 변경 (관리자 전용)
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long orderId,
                                                  @RequestBody OrderStatusUpdateDto dto) {
        orderService.updateStatus(orderId, dto.getStatus());
        return ResponseEntity.ok().build();
    }

    // ✅ 송장번호/택배사 입력 (관리자 전용)
    @PatchMapping("/{orderId}/tracking")
    public ResponseEntity<Void> updateTracking(@PathVariable Long orderId,
                                               @RequestBody TrackingUpdateDto dto) {
        orderService.updateTrackingInfo(orderId, dto.getTrackingNumber(), dto.getCourier());
        return ResponseEntity.ok().build();
    }

}
