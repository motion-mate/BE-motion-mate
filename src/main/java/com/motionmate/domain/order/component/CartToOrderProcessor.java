package com.motionmate.domain.order.component;

import com.motionmate.domain.goods.Goods;
import com.motionmate.domain.goods.Order;
import com.motionmate.domain.goods.OrderItem;
import com.motionmate.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CartToOrderProcessor {

    /**
     * 장바구니 항목 하나를 기반으로 주문 객체를 생성한다.
     *
     * @param user     주문자
     * @param goods    상품
     * @param quantity 주문 수량
     * @return Order 객체 (저장 전 상태)
     */
    public Order processSingleItem(User user, Goods goods, int quantity) {
        // 1. 주문 생성
        Order order = Order.builder()
                .user(user)
                .orderedAt(LocalDateTime.now())
                .build();

        // 2. 주문 항목 생성
        OrderItem orderItem = OrderItem.builder()
                .goods(goods)
                .quantity(quantity)
                .unitPrice(goods.getPrice()) // 할인 반영 필요 시 외부에서 계산
                .build();

        // 3. 연관관계 설정
        order.applyOrderItems(List.of(orderItem));

        return order;
    }
}
