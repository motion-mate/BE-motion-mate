    package com.motionmate.service.goods;

    import com.motionmate.domain.goods.event.EventStatus;
    import com.motionmate.domain.goods.Goods;
    import com.motionmate.domain.goods.Order;
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

    import java.util.List;

    @Service
    @RequiredArgsConstructor
    @Transactional(readOnly = true)
    public class GoodsEventService {

        private final GoodsEventRepository goodsEventRepository;
        private final GoodsEventParticipationRepository participationRepository;
        private final OrderRepository orderRepository;


        // ✅ 전체 조회
        public List<GoodsEvent> getAllEvents() {
            return goodsEventRepository.findAll();
        }

        // ✅ 상태별 조회
        public List<GoodsEvent> getEventsByStatus(EventStatus status) {
            return goodsEventRepository.findByStatus(status);
        }

        // ✅ 단건 조회 (예외 적용)
        public GoodsEvent getEventById(Long id) {
            return goodsEventRepository.findById(id)
                    .orElseThrow(() ->
                            new CustomException(HttpStatus.NOT_FOUND, "해당 이벤트가 존재하지 않습니다.")
                    );
        }

        // ✅ 등록
        @Transactional
        public GoodsEvent createEvent(GoodsEvent event) {
            return goodsEventRepository.save(event);
        }

        // ✅ 선착순 이벤트 참여
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

            participationRepository.save(
                    GoodsEventParticipation.builder()
                            .user(user)
                            .goodsEvent(event)
                            .build()
            );

            // ✅ 마지막 참여자였다면 상태 변경
            if (count + 1 == event.getStock()) {
                event.updateStatus(EventStatus.FINISHED);
            }

            // ✅ 참여 성공 시 무료 주문 자동 생성
            Goods goods = event.getGoods();

            Order order = Order.builder()
                    .user(user)
                    .goods(goods)
                    .quantity(1)
                    .build();

            orderRepository.save(order);

        }

        public int getRemainingStock(Long eventId) {
            GoodsEvent event = getEventById(eventId); // 예외처리 포함
            long participated = participationRepository.countByGoodsEvent(event);
            return event.getStock() - (int) participated;
        }
    }