package com.motionmate.controller.goods;

import com.motionmate.dto.goods.cart.CartItemRequestDto;
import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.service.goods.CartToOrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders/from-cart")
public class CartToOrderController {

    private final CartToOrderService cartToOrderService;

    @PostMapping
    public Long convertCartToOrder(
            @AuthenticationPrincipal CustomOAuth2User currentUser,
            @RequestBody List<CartItemRequestDto> cartItemRequestDtos
    ) {
        return cartToOrderService.convertCartToOrder(currentUser.getUser(), cartItemRequestDtos);
    }
}
