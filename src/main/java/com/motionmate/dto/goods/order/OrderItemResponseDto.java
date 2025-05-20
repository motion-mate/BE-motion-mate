package com.motionmate.dto.goods.order;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemResponseDto {
    private Long goodsId;
    private String goodsName;
    private int quantity;
    private int unitPrice;
    private int totalPrice;
}