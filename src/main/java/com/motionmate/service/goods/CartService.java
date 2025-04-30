package com.motionmate.service.goods;

import com.motionmate.domain.goods.*;
import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.cart.CartItemRequestDto;
import com.motionmate.dto.goods.cart.CartItemResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final GoodsRepository goodsRepository;

    // ✅ 장바구니에 상품 추가
    public void addToCart(User user, CartItemRequestDto dto) {
        Goods goods = goodsRepository.findById(dto.getGoodsId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        cartItemRepository.findByUserAndGoods(user, goods)
                .ifPresentOrElse(
                        // 이미 담긴 경우 → 수량만 증가
                        item -> item.updateQuantity(item.getQuantity() + dto.getQuantity()),
                        // 처음 담는 경우 → 새로 저장
                        () -> cartItemRepository.save(new CartItem(user, goods, dto.getQuantity()))
                );
    }

    // ✅ 수량 수정
    public void updateQuantity(User user, Long cartItemId, int quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 항목이 존재하지 않습니다."));

        if (!item.getUser().equals(user)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        item.updateQuantity(quantity);
    }

    // ✅ 장바구니 항목 삭제
    public void removeFromCart(User user, Long cartItemId) {
        cartItemRepository.deleteByUserAndId(user, cartItemId);
    }

    // ✅ 장바구니 전체 조회
    @Transactional(readOnly = true)
    public List<CartItemResponseDto> getCartList(User user) {
        return cartItemRepository.findAllByUser(user).stream()
                .map(item -> CartItemResponseDto.builder()
                        .cartItemId(item.getId())
                        .goodsId(item.getGoods().getId())
                        .name(item.getGoods().getName())
                        .imageUrl(item.getGoods().getImageUrl())
                        .price(item.getGoods().getPrice())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }
}
