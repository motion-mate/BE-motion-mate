package com.motionmate.controller.goods;

import com.motionmate.global.oauth.CustomOAuth2User; // ✅ 올바른 경로
import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.cart.CartItemRequestDto;
import com.motionmate.service.goods.CartToOrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-to-order")
@RequiredArgsConstructor
public class CartToOrderController {

    private final CartToOrderService cartToOrderService;

    @PostMapping
    public ResponseEntity<Long> convertToOrder(@AuthenticationPrincipal CustomOAuth2User user,
                                               @RequestBody List<CartItemRequestDto> cartItemRequestDtos) {
        User currentUser = user.getUser();
        Long orderId = cartToOrderService.convertCartToOrder(currentUser, cartItemRequestDtos);
        return ResponseEntity.ok(orderId); // ✅ 주문번호 반환
    }
}
