package com.motionmate.mapper.goods;

import com.motionmate.domain.goods.CartItem;
import com.motionmate.domain.goods.Goods;
import com.motionmate.dto.goods.cart.CartItemResponseDto;

public class CartItemMapper {
    public static CartItemResponseDto toDto(CartItem item) {
        return CartItemResponseDto.builder()
                .cartItemId(item.getId())
                .goodsId(item.getGoods().getId())
                .name(item.getGoods().getName())
                .imageUrl(item.getGoods().getImageUrl())
                .price(item.getGoods().getPrice())
                .quantity(item.getQuantity())
                .size(item.getSize())
                .color(item.getColor())
                .soldOut(item.getGoods().getStatus() == Goods.GoodsStatus.SOLD_OUT)
                .build();
    }
}
