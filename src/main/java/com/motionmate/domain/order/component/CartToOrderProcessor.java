package com.motionmate.domain.order.component;

import com.motionmate.domain.goods.Goods;
import com.motionmate.domain.goods.GoodsRepository;
import com.motionmate.domain.goods.OrderRepository;
import com.motionmate.domain.goods.Order;
import com.motionmate.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartToOrderProcessor {

    private final GoodsRepository goodsRepository;
    private final OrderRepository orderRepository;

    public void process(User user, Long goodsId, int quantity) {
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        Order order = new Order(user, goods, quantity);
        orderRepository.save(order);
    }
}
