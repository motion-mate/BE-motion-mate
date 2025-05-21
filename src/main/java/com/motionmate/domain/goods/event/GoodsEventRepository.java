package com.motionmate.domain.goods.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GoodsEventRepository extends JpaRepository<GoodsEvent, Long> {
    List<GoodsEvent> findByStatus(EventStatus status);

    Optional<GoodsEvent> findById(Long id);

}
