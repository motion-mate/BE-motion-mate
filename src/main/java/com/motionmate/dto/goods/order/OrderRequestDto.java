package com.motionmate.dto.goods.order;

import com.motionmate.domain.goods.Order;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderRequestDto {

    private String goodsName;
    private Integer price;
    private Order.OrderStatus status;

}
