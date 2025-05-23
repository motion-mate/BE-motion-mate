package com.motionmate.domain.goods;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
}
