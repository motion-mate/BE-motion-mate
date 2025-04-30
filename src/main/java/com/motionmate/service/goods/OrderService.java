package com.motionmate.service.goods;

import com.motionmate.domain.goods.Goods;
import com.motionmate.domain.goods.GoodsRepository;
import com.motionmate.domain.goods.Order;
import com.motionmate.domain.goods.OrderRepository;
import com.motionmate.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final GoodsRepository goodsRepository;
    private final OrderRepository orderRepository;

    // ✅ 단일 상품 주문
    public Long placeOrder(User user, Long goodsId, int quantity) {
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        goods.decreaseStock(quantity); // 재고 차감, 부족하면 예외 발생

        Order order = new Order(user, goods, quantity);
        return orderRepository.save(order).getId();
    }
}
