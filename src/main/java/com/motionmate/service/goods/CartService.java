// CartService.java
package com.motionmate.service.goods;

import com.motionmate.domain.goods.*;
import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.cart.CartItemRequestDto;
import com.motionmate.dto.goods.cart.CartItemResponseDto;
import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.mapper.goods.CartItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final GoodsRepository goodsRepository;

    public void addToCart(User user, CartItemRequestDto dto) {
        Goods goods = goodsRepository.findById(dto.getGoodsId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        cartItemRepository.findByUserAndGoodsAndSizeAndColor(user, goods, dto.getSize(), dto.getColor())
                .ifPresentOrElse(
                        item -> item.updateQuantity(item.getQuantity() + dto.getQuantity()),
                        () -> {
                            CartItem newItem = new CartItem(user, goods, dto.getQuantity(), dto.getSize(), dto.getColor());
                            cartItemRepository.save(newItem);
                        }
                );
    }

    public void updateQuantity(User user, Long cartItemId, int quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 항목이 존재하지 않습니다."));

        if (!item.getUser().equals(user)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        item.updateQuantity(quantity);
    }

    public void removeFromCart(User user, Long cartItemId) {
        cartItemRepository.deleteByUserAndId(user, cartItemId);
    }

    @Transactional(readOnly = true)
    public List<CartItemResponseDto> getCartList(User user) {
        return cartItemRepository.findAllByUser(user).stream()
                .map(CartItemMapper::toDto)
                .toList();
    }

    public void removeFromCartList(User user, List<Long> cartItemId) {
        cartItemRepository.deleteByIdInAndUser(cartItemId, user);
    }
}
