package com.motionmate.controller.goods;

import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.cart.CartItemRequestDto;
import com.motionmate.dto.goods.cart.CartItemResponseDto;
import com.motionmate.service.goods.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // ✅ 장바구니 항목 추가
    @PostMapping
    public void addToCart(@AuthenticationPrincipal User user,
                          @RequestBody @Valid CartItemRequestDto dto) {
        cartService.addToCart(user, dto);
    }

    // ✅ 수량 수정
    @PatchMapping("/{cartItemId}")
    public void updateQuantity(@AuthenticationPrincipal User user,
                               @PathVariable Long cartItemId,
                               @RequestBody @Valid CartItemRequestDto dto) {
        cartService.updateQuantity(user, cartItemId, dto.getQuantity());
    }

    // ✅ 항목 삭제
    @DeleteMapping("/{cartItemId}")
    public void removeCartItem(@AuthenticationPrincipal User user,
                               @PathVariable Long cartItemId) {
        cartService.removeFromCart(user, cartItemId);
    }

    // ✅ 장바구니 목록 조회
    @GetMapping
    public List<CartItemResponseDto> getCartList(@AuthenticationPrincipal User user) {
        return cartService.getCartList(user);
    }
}
