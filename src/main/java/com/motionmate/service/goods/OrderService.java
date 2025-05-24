package com.motionmate.service.goods;

import com.motionmate.domain.delivery.Delivery;
import com.motionmate.domain.goods.Goods;
import com.motionmate.domain.goods.Order;
import com.motionmate.domain.goods.OrderItem;
import com.motionmate.domain.goods.OrderRepository;
import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.order.OrderRequestDto;
import com.motionmate.dto.goods.order.OrderResponseDto;
import com.motionmate.dto.goods.order.OrderItemRequestDto;
import com.motionmate.global.exception.CustomException;
import com.motionmate.mapper.delivery.DeliveryMapper;
import com.motionmate.mapper.goods.OrderMapper;
import com.motionmate.domain.goods.GoodsRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final GoodsRepository goodsRepository;
    private final OrderMapper orderMapper;

    /**
     * 주문 생성
     */
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto, User user) {
        // 1. 상품 ID 목록 추출
        List<Long> goodsIds = requestDto.getItems().stream()
                .map(OrderItemRequestDto::getGoodsId)
                .toList();

        // 2. 상품 목록 조회
        List<Goods> goodsList = goodsRepository.findAllById(goodsIds);

        // 3. 주문 객체 생성
        Order order = orderMapper.toOrderEntity(user);

        // 4. 주문상품 생성
        List<OrderItem> orderItems = orderMapper.toOrderItemEntityList(requestDto.getItems(), goodsList);
        order.applyOrderItems(orderItems);

        // ✅ 5. 배송 정보 생성 및 연결
        Delivery delivery = DeliveryMapper.toEntity(requestDto.getDelivery()); // delivery 필드 추가돼야 함
        order.applyDelivery(delivery);

        // 6. 저장
        orderRepository.save(order);

        // 7. 응답 DTO 변환
        return orderMapper.toResponseDto(order);
    }

    /**
     * 사용자의 모든 주문 목록 조회
     */
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrders(User user) {
        return orderRepository.findByUser(user).stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public void updateStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));
        order.updateStatus(status); // setter 없으면 직접 필드 수정
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(Long orderId, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,"해당 주문을 찾을 수 없습니다."));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new CustomException(HttpStatus.FORBIDDEN,"해당 주문에 접근할 수 없습니다.");
        }

        return orderMapper.toResponseDto(order);
    }

}