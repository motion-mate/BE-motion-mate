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

        // ✅ 핵심: 이거 하나면 충분
        return ResponseEntity.ok(orderService.createOrder(requestDto, user));
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

    // ✅ 주문 상세 조회 (프론트에서 /api/orders/{id}로 요청)
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id,
                                                         @AuthenticationPrincipal CustomOAuth2User userPrincipal) {
        User user = userPrincipal.getUser();
        return ResponseEntity.ok(orderService.getOrderById(id, user));
    }



}
