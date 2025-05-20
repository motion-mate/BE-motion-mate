package com.motionmate.domain.goods.event;

import com.motionmate.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsEventParticipationRepository extends JpaRepository<GoodsEventParticipation, Long> {

    // ✅ 중복 참여 방지용
    boolean existsByUserAndGoodsEvent(User user, GoodsEvent goodsEvent);

    // ✅ 선착순 제한 체크용
    long countByGoodsEvent(GoodsEvent goodsEvent);
}
