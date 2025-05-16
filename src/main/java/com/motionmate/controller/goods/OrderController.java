package com.motionmate.controller.goods;

import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.order.OrderResponseDto;
import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.service.goods.OrderService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // ✅ 단일 상품 주문
    @PostMapping
    public Long placeOrder(@AuthenticationPrincipal CustomOAuth2User user,
                           @RequestParam Long goodsId,
                           @RequestParam @Min(1) int quantity) {
        return orderService.placeOrder(user.getUser(), goodsId, quantity);
    }

    // ✅ 주문 내역 조회 (GET 추가)
    @GetMapping
    public List<OrderResponseDto> getOrders(@AuthenticationPrincipal CustomOAuth2User user) {
        return orderService.getOrders(user.getUser());
    }
}
