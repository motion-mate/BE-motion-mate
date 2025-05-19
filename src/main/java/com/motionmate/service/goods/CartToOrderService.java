package com.motionmate.service.goods;


import com.motionmate.domain.order.component.CartToOrderProcessor;
import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.cart.CartItemRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartToOrderService {

    private final CartToOrderProcessor cartToOrderProcessor;

    @Transactional
    public Long convertCartToOrder(User user, List<CartItemRequestDto> cartItemRequestDtos) {
        Long orderId = null;

        for (CartItemRequestDto dto : cartItemRequestDtos) {
            // 여러 상품 중 마지막 주문 ID 반환
            orderId = cartToOrderProcessor.process(user, dto.getGoodsId(), dto.getQuantity());
        }

        return orderId;
    }
}
