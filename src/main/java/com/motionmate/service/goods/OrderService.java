package com.motionmate.service.goods;

import com.motionmate.domain.goods.Goods;
import com.motionmate.domain.goods.GoodsRepository;
import com.motionmate.domain.goods.Order;
import com.motionmate.domain.goods.OrderRepository;
import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.order.OrderResponseDto;
import com.motionmate.mapper.goods.OrderMapper; // 🔥 OrderMapper import 추가
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final GoodsRepository goodsRepository;

    // ✅ 주문 생성
    @Transactional
    public Long placeOrder(User user, Long goodsId, int quantity) {
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        if (goods.getStock() < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        // 재고 차감
        goods.decreaseStock(quantity);

        Order order = new Order(user, goods, quantity);
        orderRepository.save(order);

        return order.getId();
    }

    // ✅ 주문 내역 조회
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrders(User user) {
        return orderRepository.findByUser(user).stream()
                .map(OrderMapper::toDto) // 🔥 매핑 로직 위임
                .collect(Collectors.toList());
    }
}
