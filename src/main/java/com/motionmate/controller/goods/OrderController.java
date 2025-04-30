package com.motionmate.controller.goods;

import com.motionmate.domain.user.User;
import com.motionmate.service.goods.OrderService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // ✅ 단일 상품 주문 요청
    @PostMapping
    public Long placeOrder(@AuthenticationPrincipal User user,
                           @RequestParam Long goodsId,
                           @RequestParam @Min(1) int quantity) {
        return orderService.placeOrder(user, goodsId, quantity);
    }
}
