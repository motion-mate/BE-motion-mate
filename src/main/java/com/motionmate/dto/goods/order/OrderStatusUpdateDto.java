package com.motionmate.dto.goods.order;

import com.motionmate.domain.goods.Order;
import lombok.Getter;

@Getter
public class OrderStatusUpdateDto {
    private Order.OrderStatus status;
}
