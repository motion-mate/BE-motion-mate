package com.motionmate.service.goods;

import com.motionmate.domain.goods.Goods;
import com.motionmate.domain.goods.Order;
import com.motionmate.domain.goods.OrderItem;
import com.motionmate.domain.goods.OrderRepository;
import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.cart.CartItemRequestDto;
import com.motionmate.domain.goods.GoodsRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartToOrderService {

    private final OrderRepository orderRepository;
    private final GoodsRepository goodsRepository;

    @Transactional
    public Long convertCartToOrder(User user, List<CartItemRequestDto> cartItems) {
        // 1. Order 생성
        Order order = Order.builder()
                .user(user)
                .orderedAt(LocalDateTime.now())
                .build();

        // 2. OrderItem 리스트 생성
        List<OrderItem> orderItemList = cartItems.stream().map(item -> {
            Goods goods = goodsRepository.findById(item.getGoodsId())
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다: id=" + item.getGoodsId()));

            return OrderItem.builder()
                    .goods(goods)
                    .quantity(item.getQuantity())
                    .unitPrice(goods.getPrice())
                    .build();
        }).toList();

        // 3. 연관관계 세팅
        order.applyOrderItems(orderItemList);

        // 4. 저장
        orderRepository.save(order);

        // 5. 주문 ID 반환
        return order.getId();
    }
}
