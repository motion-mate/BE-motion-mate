package com.motionmate.domain.goods;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
    List<Goods> findTop3ByOrderByCreatedAtDesc();
}
