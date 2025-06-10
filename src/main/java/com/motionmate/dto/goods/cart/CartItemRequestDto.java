package com.motionmate.dto.goods.cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequestDto {
    private Long goodsId;
    private int quantity;
    private String size;
    private String color;
}
