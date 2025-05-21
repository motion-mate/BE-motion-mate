package com.motionmate.service.goods;

import com.motionmate.domain.goods.event.EventStatus;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
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
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "해당 이벤트가 존재하지 않습니다."));
    }

    @Transactional
    public GoodsEvent createEvent(GoodsEvent event) {
        return goodsEventRepository.save(event);
    }

    @Transactional
    public void participate(User user, Long eventId) {
        log.info("✅ 이벤트 참여 시도: userId={}, eventId={}", user.getId(), eventId);

        GoodsEvent event = getEventById(eventId);


        log.info("🟡 현재 재고: {}", event.getEventStock()); // ✅ 재고 로그 여기!

        if (event.getEventStock() <= 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이벤트 재고가 소진되었습니다.");
        }

        event.decreaseStock(1);

        participationRepository.save(GoodsEventParticipation.builder()
                .user(user)
                .goodsEvent(event)
                .build());

        Order order = Order.builder()
                .user(user)
                .orderedAt(LocalDateTime.now())
                .status(Order.OrderStatus.READY)
                .orderNumber("EVENT-" + System.currentTimeMillis())
                .build();

        OrderItem item = OrderItem.builder()
                .goodsName(event.getGoodsName()) // ✅ 연관관계 없이 이름만 저장
                .quantity(1)
                .unitPrice(0)
                .build();

        order.applyOrderItems(List.of(item));
        orderRepository.save(order);

        if (event.getEventStock() == 0) {
            event.updateStatus(EventStatus.FINISHED);
        }
    }

    public int getRemainingStock(Long eventId) {
        GoodsEvent event = getEventById(eventId);
        return event.getEventStock();
    }
}
