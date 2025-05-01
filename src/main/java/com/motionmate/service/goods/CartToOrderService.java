package com.motionmate.service.goods;

import com.motionmate.domain.user.User;
import com.motionmate.domain.order.component.CartToOrderProcessor;
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
    public void convertCartToOrder(User user, List<CartItemRequestDto> cartItemRequestDtos) {
        for (CartItemRequestDto dto : cartItemRequestDtos) {
            cartToOrderProcessor.process(user, dto.getGoodsId(), dto.getQuantity());
        }
    }
}
