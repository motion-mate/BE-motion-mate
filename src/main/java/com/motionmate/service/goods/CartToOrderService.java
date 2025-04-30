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

    // ✅ 장바구니 → 주문 전환
    public List<Long> convertCartToOrder(User user, List<Long> cartItemIds) {
        List<Order> createdOrders = new ArrayList<>();

        for (Long cartItemId : cartItemIds) {
            CartItem cartItem = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> new IllegalArgumentException("장바구니 항목이 존재하지 않습니다."));

            // 본인 항목인지 검증
            if (!cartItem.getUser().equals(user)) {
                throw new IllegalArgumentException("해당 장바구니 항목에 대한 권한이 없습니다.");
            }

            Goods goods = cartItem.getGoods();
            int quantity = cartItem.getQuantity();

            // 재고 차감 (내부에서 예외 처리됨)
            goods.decreaseStock(quantity);

            // 주문 객체 생성
            Order order = new Order(user, goods, quantity);
            createdOrders.add(order);

            // 장바구니 항목 삭제
            cartItemRepository.delete(cartItem);
        }

        // 한 번에 주문 저장
        orderRepository.saveAll(createdOrders);

        // 생성된 주문 ID 목록 반환
        return createdOrders.stream()
                .map(Order::getId)
                .toList();
    }
}
