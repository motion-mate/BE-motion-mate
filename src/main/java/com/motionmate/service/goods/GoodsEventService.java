package com.motionmate.service.goods;

import com.motionmate.domain.goods.event.EventStatus;
import com.motionmate.domain.goods.Goods;
import com.motionmate.domain.goods.Order;
import com.motionmate.domain.goods.OrderItem;
import com.motionmate.domain.goods.OrderRepository;
import com.motionmate.domain.goods.event.GoodsEvent;
import com.motionmate.domain.goods.event.GoodsEventParticipation;
import com.motionmate.domain.goods.event.GoodsEventParticipationRepository;
import com.motionmate.domain.goods.event.GoodsEventRepository;
import com.motionmate.domain.user.User;
import com.motionmate.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoodsEventService {

    private final GoodsEventRepository goodsEventRepository;
    private final GoodsEventParticipationRepository participationRepository;
    private final OrderRepository orderRepository;

    public List<GoodsEvent> getAllEvents() {
        return goodsEventRepository.findAll();
    }

    public List<GoodsEvent> getEventsByStatus(EventStatus status) {
        return goodsEventRepository.findByStatus(status);
    }

    public GoodsEvent getEventById(Long id) {
        return goodsEventRepository.findById(id)
                .orElseThrow(() ->
                        new CustomException(HttpStatus.NOT_FOUND, "해당 이벤트가 존재하지 않습니다."));
    }

    @Transactional
    public GoodsEvent createEvent(GoodsEvent event) {
        return goodsEventRepository.save(event);
    }

    /**
     * 선착순 이벤트 참여 처리 (주문 포함)
     */
    @Transactional
    public void participate(User user, Long eventId) {
        GoodsEvent event = getEventById(eventId);

        if (participationRepository.existsByUserAndGoodsEvent(user, event)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 참여한 이벤트입니다.");
        }

        long count = participationRepository.countByGoodsEvent(event);
        if (count >= event.getStock()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "참여 인원이 마감되었습니다.");
        }

        // 이벤트 참여 저장
        participationRepository.save(
                GoodsEventParticipation.builder()
                        .user(user)
                        .goodsEvent(event)
                        .build()
        );

        // 마지막 참여자면 상태 변경
        if (count + 1 == event.getStock()) {
            event.updateStatus(EventStatus.FINISHED);
        }

        // 무료 주문 생성
        Goods goods = event.getGoods();

        Order order = Order.builder()
                .user(user)
                .orderedAt(LocalDateTime.now())
                .status(Order.OrderStatus.READY)
                .orderNumber("EVENT-" + System.currentTimeMillis())
                .build();

        OrderItem orderItem = OrderItem.builder()
                .goods(goods)
                .quantity(1)
                .unitPrice(0)
                .build();

        order.applyOrderItems(List.of(orderItem));
        orderRepository.save(order);
    }

    public int getRemainingStock(Long eventId) {
        GoodsEvent event = getEventById(eventId);
        long participated = participationRepository.countByGoodsEvent(event);
        return event.getStock() - (int) participated;
    }
}
