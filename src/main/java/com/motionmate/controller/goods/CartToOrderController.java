package com.motionmate.controller.goods;

import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.cart.CartItemRequestDto;
import com.motionmate.service.goods.CartToOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart-to-order")
public class CartToOrderController {

    private final CartToOrderService cartToOrderService;

    @PostMapping
    public ResponseEntity<Void> convertToOrder(@AuthenticationPrincipal User user,
                                               @RequestBody List<CartItemRequestDto> cartItemRequestDtos) {
        cartToOrderService.convertCartToOrder(user, cartItemRequestDtos);
        return ResponseEntity.ok().build();
    }
}
