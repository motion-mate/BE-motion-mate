package com.motionmate.dto.goods.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderItemResponseDto {

    private Long goodsId;
    private String goodsName;
    private String goodsImageUrl;
    private int quantity;
    private int unitPrice;
    private int totalPrice;

    private boolean hasReview;
}
