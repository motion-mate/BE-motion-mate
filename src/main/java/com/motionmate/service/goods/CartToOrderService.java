package com.motionmate.service.goods;

import com.motionmate.domain.goods.*;
import com.motionmate.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartToOrderService {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public List<Long> convertCartToOrder(User user, List<Long> cartItemIds) {
        List<Order> createdOrders = new ArrayList<>();

        for (Long cartItemId : cartItemIds) {
            CartItem cartItem = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> new IllegalArgumentException("장바구니 항목이 존재하지 않습니다."));

            // 사용자 검증
            if (!cartItem.getUser().equals(user)) {
                throw new IllegalArgumentException("해당 장바구니 항목에 대한 권한이 없습니다.");
            }

            Goods goods = cartItem.getGoods();
            int quantity = cartItem.getQuantity();

            // 재고 차감
            goods.decreaseStock(quantity);

            // 주문 생성
            Order order = new Order(user, goods, quantity);
            createdOrders.add(order);

            // 장바구니 항목 삭제
            cartItemRepository.delete(cartItem);
        }

        orderRepository.saveAll(createdOrders);

        return createdOrders.stream()
                .map(Order::getId)
                .toList();
    }
}
