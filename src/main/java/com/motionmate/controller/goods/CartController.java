package com.motionmate.controller.goods;

import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.cart.CartItemRequestDto;
import com.motionmate.dto.goods.cart.CartItemResponseDto;
import com.motionmate.global.oauth.CustomOAuth2User;
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
    public void addToCart(@AuthenticationPrincipal CustomOAuth2User user,
                          @RequestBody @Valid CartItemRequestDto dto) {
        cartService.addToCart(user.getUser(), dto);
    }

    // ✅ 수량 수정
    @PatchMapping("/{cartItemId}")
    public void updateQuantity(@AuthenticationPrincipal CustomOAuth2User user,
                               @PathVariable Long cartItemId,
                               @RequestBody @Valid CartItemRequestDto dto) {
        cartService.updateQuantity(user.getUser(), cartItemId, dto.getQuantity());
    }

    // ✅ 항목 삭제
    @DeleteMapping("/{cartItemId}")
    public void removeCartItem(@AuthenticationPrincipal CustomOAuth2User user,
                               @PathVariable Long cartItemId) {
        cartService.removeFromCart(user.getUser(), cartItemId);
    }

    // ✅ 장바구니 목록 조회
    @GetMapping
    public List<CartItemResponseDto> getCartList(@AuthenticationPrincipal CustomOAuth2User customUser) {
        System.out.println(">>>> user id: " + customUser.getUser().getId());
        return cartService.getCartList(customUser.getUser());
    }

    @DeleteMapping
    public void removeCartItems(@AuthenticationPrincipal CustomOAuth2User user,
                                @RequestBody List<Long> cartItemId) {
        cartService.removeFromCartList(user.getUser(), cartItemId);
    }
}
