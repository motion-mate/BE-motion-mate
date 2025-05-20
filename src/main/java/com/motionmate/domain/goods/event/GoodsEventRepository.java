package com.motionmate.domain.goods.event;

import com.motionmate.domain.goods.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodsEventRepository extends JpaRepository<GoodsEvent, Long> {
    List<GoodsEvent> findByStatus(EventStatus status);

}
