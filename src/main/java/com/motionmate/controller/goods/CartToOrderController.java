package com.motionmate.controller.goods;

import com.motionmate.domain.user.User;
import com.motionmate.service.goods.CartToOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-to-order")
@RequiredArgsConstructor
public class CartToOrderController {

    private final CartToOrderService cartToOrderService;

    // ✅ 장바구니 항목들 → 주문 전환
    @PostMapping
    public List<Long> convertToOrder(@AuthenticationPrincipal User user,
                                     @RequestBody List<Long> cartItemIds) {
        return cartToOrderService.convertCartToOrder(user, cartItemIds);
    }
}